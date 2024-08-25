package com.booking.listingservice.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegionCreationRequest {
    String imgSrc;
    String name;
    String location;
    String label;
}
