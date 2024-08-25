package com.booking.bookingservice.dto.request;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
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
public class ReservationRequest {
    @NotNull(message = "LISTINGID_REQUIRED")
    String listingId;

    String guestName;
    
    @NotNull(message = "PHONE_NUMBER_REQUIRED")
    String phoneNumber;

    @NotNull(message = "CHECKIN_DATE_REQUIRED")
    LocalDateTime checkInDate;
    @NotNull(message = "CHECKOUT_DATE_REQUIRED")
    LocalDateTime checkOutDate;

    Integer adultCount;
    Integer childrenCount;
    
    @NotNull(message = "PRICE_REQUIRED")
    String totalPrice;
}
