package com.booking.bookingservice.dto.request;

import java.time.LocalDateTime;

import com.booking.bookingservice.enums.ListingStatus;

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
public class UpdateListingStatusRequest {
    String listingId;

    @Builder.Default
    ListingStatus status = ListingStatus.BOOKED;

    LocalDateTime checkinDate;
    LocalDateTime checkoutDate;
}
