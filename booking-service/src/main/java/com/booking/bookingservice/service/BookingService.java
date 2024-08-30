package com.booking.bookingservice.service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.booking.bookingservice.dto.request.ConfirmReservationRequest;
import com.booking.bookingservice.dto.request.DenyReservationRequest;
import com.booking.bookingservice.dto.request.ReservationRequest;
import com.booking.bookingservice.dto.request.UpdateListingStatusRequest;
import com.booking.bookingservice.dto.response.ListingResponse;
import com.booking.bookingservice.dto.response.ReservationResponse;
import com.booking.bookingservice.entity.Reservation;
import com.booking.bookingservice.entity.ConfirmReservationToken;
import com.booking.bookingservice.enums.ListingStatus;
import com.booking.bookingservice.enums.ReservationStatus;
import com.booking.bookingservice.exception.AppException;
import com.booking.bookingservice.exception.ErrorCode;
import com.booking.bookingservice.mapper.ReservationMapper;
import com.booking.bookingservice.repository.ConfirmReservationTokenRepository;
import com.booking.bookingservice.repository.ReservationRepository;
import com.booking.bookingservice.repository.httpclient.ListingClient;
import com.booking.event.dto.NotificationEvent;

import feign.FeignException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
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
    TokenService tokenService;

    @NonFinal
    @Value("${app.topic.bookingApproval}")
    String bookingApprovalTopic;

    @NonFinal
    @Value("${app.topic.bookingConfirmation}")
    String bookingConfirmationTopic;
    
    @NonFinal
    @Value("${app.topic.bookingDeny}")
    String bookingDenyTopic;

    @NonFinal
    @Value("${app.templateCode.bookingApproval}")
    String bookingApprovalTemplateCode;

    @NonFinal
    @Value("${app.templateCode.bookingConfirmation}")
    String bookingConfirmationTemplateCode;

    @NonFinal
    @Value("${app.templateCode.bookingDeny}")
    String bookingDenyTemplateCode;


    public ReservationResponse reservation(ReservationRequest request) {
        try {
            ListingResponse listing = listingClient.getListing(request.getListingId());
            // xem phòng còn trống không
            if (listing.getStatus() != ListingStatus.AVAILABLE) {
                throw new AppException(ErrorCode.NOT_AVAILABLE_LISTING);
            }

            Reservation reservation = reservationMapper.toReservation(request);
            reservation.setRenterId(tokenService.getUserIdFromToken());

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
                    .receiver(tokenService.getUserEmailFromToken())
                    .params(params)
                    .templateCode(bookingApprovalTemplateCode)
                    .build();
            kafkaTemplate.send(bookingApprovalTopic, notificationEvent);

            return reservationMapper.toReservationResponse(reservation);

        } catch (FeignException.NotFound e) {
            throw new AppException(ErrorCode.NOT_FOUND_LISTING);
        } catch (FeignException e) {
            throw new AppException(ErrorCode.LISTING_SERVICE_ERROR);
        }
    }

    public void confirmReservation(ConfirmReservationRequest request) {
        Optional<ConfirmReservationToken> optionalToken = confirmReservationTokenRepository
                .findFirstByReservationIdAndExpiryDateAfter(request.getReservationId(), Instant.now());
        if (!optionalToken.isPresent()) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }

        ConfirmReservationToken confirmToken = optionalToken.get();

        if (!passwordEncoder.matches(request.getToken(), confirmToken.getToken())) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
        // update reservation
        Reservation reservation = reservationRepository.findById(request.getReservationId())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_RESERVATION));
        reservation.setStatus(request.getStatus());
        reservationRepository.save(reservation);
        // update listing status
        UpdateListingStatusRequest updateListingStatusRequest = UpdateListingStatusRequest.builder()
                .listingId(reservation.getListingId())
                .build();

        listingClient.setListingStatusToBooked(updateListingStatusRequest);

        // send mail to renter
        Map<String, Object> params = new HashMap<>();
        params.put("subject", "Yêu cầu thuê phòng đã được xác nhận");

        NotificationEvent notificationEvent = NotificationEvent.builder()
                .channel("EMAIL")
                .receiver(tokenService.getUserEmailFromToken())
                .params(params)
                .templateCode(bookingConfirmationTemplateCode)
                .build();
        kafkaTemplate.send(bookingConfirmationTopic, notificationEvent);

    }

    public void denyReservation(DenyReservationRequest request) {
        Optional<ConfirmReservationToken> optionalToken = confirmReservationTokenRepository
                .findFirstByReservationIdAndExpiryDateAfter(request.getReservationId(), Instant.now());
        if (!optionalToken.isPresent()) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }

        ConfirmReservationToken confirmToken = optionalToken.get();

        if (!passwordEncoder.matches(request.getToken(), confirmToken.getToken())) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
        // update reservation
        Reservation reservation = reservationRepository.findById(request.getReservationId())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_RESERVATION));
        reservation.setStatus(request.getStatus());

        // send mail to renter
        Map<String, Object> params = new HashMap<>();
        reservationRepository.save(reservation);

        params.put("subject", "Yêu cầu thuê phòng đã bị từ chối");

        NotificationEvent notificationEvent = NotificationEvent.builder()
                .channel("EMAIL")
                .receiver(tokenService.getUserEmailFromToken())
                .params(params)
                .templateCode(bookingDenyTemplateCode)
                .build();
        kafkaTemplate.send(bookingDenyTopic, notificationEvent);
    }
}
