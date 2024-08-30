package com.booking.bookingservice.dto.response;

import java.time.Instant;

import com.booking.bookingservice.enums.ReservationStatus;
import com.booking.bookingservice.enums.PaymentStatus;

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
public class ReservationResponse {
    String listingId;
    String renterId;

    Integer adultCount;
    Integer childrenCount;
    Double totalPrice;

    ReservationStatus status;
    PaymentStatus paymentStatus;
     
    Instant checkInDate;
    Instant checkOutDate;

    Instant createdAt;
    Instant updatedAt;

    boolean hasCheckout;
}
