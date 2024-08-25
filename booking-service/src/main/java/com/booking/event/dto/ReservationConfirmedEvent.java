package com.booking.event.dto;

import java.time.LocalDateTime;
import com.booking.bookingservice.enums.PaymentStatus;
import com.booking.bookingservice.enums.ReservationStatus;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReservationConfirmedEvent {
    String reservationId;

    String renterId;
    String listingId;

    @Buider.Default 
    ReservationStatus status = ReservationStatus.CONFIRMED;
    @Buider.Default 
    PaymentStatus paymentStatus = PaymentStatus;
}
