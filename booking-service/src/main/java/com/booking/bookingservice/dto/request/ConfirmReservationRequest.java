package com.booking.bookingservice.dto.request;

import com.booking.bookingservice.enums.ReservationStatus;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConfirmReservationRequest {
    String reservationId;
    String token;

    @Builder.Default
    ReservationStatus status = ReservationStatus.CONFIRMED;
}

