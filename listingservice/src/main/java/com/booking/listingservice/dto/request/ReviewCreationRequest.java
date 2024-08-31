package com.booking.listingservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReviewCreationRequest {

    @NotNull(message ="LISTINGID_REQUIRED")
    String listingId;
    
    @NotBlank(message = "CONTENT_NOT_BLANK")
    String content;

    Integer ratingPoint;
}
