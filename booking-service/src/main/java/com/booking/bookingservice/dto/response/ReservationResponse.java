package com.booking.bookingservice.dto.response;

import java.time.LocalDateTime;

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
    String totalPrice;

    ReservationStatus status;
    PaymentStatus paymentStatus;
     
    LocalDateTime checkInDate;
    LocalDateTime checkOutDate;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    boolean hasCheckout;
}
