package com.booking.listingservice.dto.request;


import java.time.Instant;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RecentSearchCreationRequest {
    String city;

    Instant checkin;
    Instant checkout;

    Integer adultsCount;
    Integer childrensCount;
}
