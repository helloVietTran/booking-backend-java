package com.booking.bookingservice.controller;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.booking.bookingservice.dto.ApiResponse;
import com.booking.bookingservice.dto.request.ConfirmReservationRequest;
import com.booking.bookingservice.dto.request.DenyReservationRequest;
import com.booking.bookingservice.dto.request.ReservationRequest;
import com.booking.bookingservice.dto.response.ReservationResponse;
import com.booking.bookingservice.service.BookingService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;


@RestController
@RequestMapping("/reservation")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingController {
    BookingService bookingService;

    @PostMapping
    ApiResponse<ReservationResponse> reservation(@RequestBody ReservationRequest request){
        return ApiResponse.<ReservationResponse>builder()
                         .result(bookingService.reservation(request))
                         .build();
    }

    //
    @PatchMapping("/confirm")
    ApiResponse<String> confirmReservation(@RequestBody ConfirmReservationRequest request){
        bookingService.confirmReservation(request);

        return ApiResponse.<String>builder()
                         .result("Confirm reservation sucessfull")
                         .build();
    }

    @PatchMapping("/deny")
    ApiResponse<String> DenyReservation(@RequestBody DenyReservationRequest request){
        bookingService.denyReservation(request);

        return ApiResponse.<String>builder()
                         .result("Confirm reservation sucessfull")
                         .build();
    }
}
