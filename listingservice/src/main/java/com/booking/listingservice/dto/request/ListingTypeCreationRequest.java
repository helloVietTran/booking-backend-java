package com.booking.listingservice.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ListingTypeCreationRequest {
    @NotNull(message = "Name is required")
    String name;
    
    String description;
    String imgSrc;
    String label;
}
