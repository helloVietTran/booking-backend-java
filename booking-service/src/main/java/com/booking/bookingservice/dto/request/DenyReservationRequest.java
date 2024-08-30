package com.booking.bookingservice.dto.request;

import com.booking.bookingservice.enums.ReservationStatus;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DenyReservationRequest {
    String reservationId;
    String token;
    @Builder.Default
    ReservationStatus status = ReservationStatus.CANCELED;
}
