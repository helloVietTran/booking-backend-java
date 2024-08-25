package com.booking.listingservice.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegionResponse {
    String id;
    String imgSrc;
    String name;
    String location;
    Integer totalListings;
}
