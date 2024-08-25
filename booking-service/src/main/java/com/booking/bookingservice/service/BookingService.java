package com.booking.bookingservice.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.booking.bookingservice.dto.request.ConfirmReservationRequest;
import com.booking.bookingservice.dto.request.ReservationRequest;
import com.booking.bookingservice.dto.request.UpdateListingStatusRequest;
import com.booking.bookingservice.dto.response.ListingResponse;
import com.booking.bookingservice.dto.response.ReservationResponse;
import com.booking.bookingservice.entity.Reservation;
import com.booking.bookingservice.entity.ConfirmReservationToken;
import com.booking.bookingservice.enums.ListingStatus;
import com.booking.bookingservice.exception.AppException;
import com.booking.bookingservice.exception.ErrorCode;
import com.booking.bookingservice.mapper.ReservationMapper;
import com.booking.bookingservice.repository.ConfirmReservationTokenRepository;
import com.booking.bookingservice.repository.ReservationRepository;
import com.booking.bookingservice.repository.httpclient.ListingClient;
import com.booking.event.dto.NotificationEvent;
import com.booking.event.dto.ReservationConfirmedEvent;

import feign.FeignException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingService {
    ReservationRepository reservationRepository;
    ConfirmReservationTokenRepository confirmReservationTokenRepository;

    PasswordEncoder passwordEncoder;
    ReservationMapper reservationMapper;
    ListingClient listingClient;
    
    KafkaTemplate<String, Object> kafkaTemplate;

    String getUserIdFromToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof Jwt) {
            Jwt jwt = (Jwt) authentication.getPrincipal();
            return jwt.getClaim("id");
        }
        return null;
    }

    public String getUserEmailFromToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    
        if (authentication != null && authentication.getPrincipal() instanceof Jwt) {
            Jwt jwt = (Jwt) authentication.getPrincipal();
            return jwt.getSubject();
        }
        return null;
    }
  

    public ReservationResponse reservation(ReservationRequest request) {
        
        Reservation reservation = reservationMapper.toReservation(request);
        reservation.setRenterId(getUserIdFromToken());

        try {
            ListingResponse listing = listingClient.getListing(request.getListingId());

            if(listing.getStatus() != ListingStatus.AVAILABLE){
              throw new AppException(ErrorCode.NOT_AVAILABLE_LISTING);
            }
           

            reservationRepository.save(reservation);
            // built token
            String token = UUID.randomUUID().toString();
            ConfirmReservationToken reservationConfirmToken = ConfirmReservationToken.builder()
                                    .token(passwordEncoder.encode(token))
                                    .reservationId(reservation.getId())
                                    .build();
            
            confirmReservationTokenRepository.save(reservationConfirmToken);

            // send topic
            Map<String, Object> params = new HashMap<>();
            params.put("subject", "Xác nhận đồng ý cho thuê phòng");
            params.put("token", token);
            params.put("reservationId", reservation.getId());

            NotificationEvent notificationEvent = NotificationEvent.builder()
                    .channel("EMAIL")
                    .receiver(getUserEmailFromToken())
                    .params(params)
                    .templateCode("listing-rental-approval")
                    .build();
            kafkaTemplate.send("notification-listing-rental-approval", notificationEvent);

            return reservationMapper.toReservationResponse(reservation);

        } catch (FeignException.NotFound e) {
            throw new AppException(ErrorCode.NOT_FOUND_LISTING);
        } catch (FeignException e) {
            throw new AppException(ErrorCode.LISTING_SERVICE_ERROR);
        }   
    }

    public void confirmReservation(ConfirmReservationRequest request){
        LocalDateTime currentTime = LocalDateTime.now();

        Optional<ConfirmReservationToken> optionalToken = confirmReservationTokenRepository
                                    .findFirstByReservationIdAndExpiryDateAfter(request.getReservationId(), currentTime);
        if(!optionalToken.isPresent()){
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }

        ConfirmReservationToken confirmToken = optionalToken.get();

        if(!passwordEncoder.matches(request.getToken(), confirmToken.getToken())){
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }

        Reservation reservation = reservationRepository.findById(request.getReservationId())
                    .orElseThrow(()-> new AppException(ErrorCode.NOT_FOUND_RESERVATION));
        // update status
        UpdateListingStatusRequest updateListingStatusRequest = UpdateListingStatusRequest.builder()
                                            .listingId(reservation.getListingId())
                                            .build();
        

        listingClient.setListingStatusToBooked(updateListingStatusRequest);

        // phát sự kiện chấp nhận khi thành công
        ReservationConfirmedEvent reservationConfirmedEvent = ReservationConfirmedEvent.builder()
                                        .renterId(reservation.getRenterId())
                                        .reservationId(reservation.getId())
                                        .listingId(reservation.getListingId())
                                        .build();

        kafkaTemplate.send("reservation_confirmed_event", notificationEvent);
    }
}
