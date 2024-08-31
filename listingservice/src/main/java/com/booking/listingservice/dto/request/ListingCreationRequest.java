package com.booking.listingservice.dto.request;

import java.time.Instant;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ListingCreationRequest {
    @NotNull(message = "title isRequired")
    String title;

    @NotNull(message = "description isRequired")
    String description;

    @NotNull(message = "address isRequired")
    String address;

    @NotNull(message = "city isRequired")
    String city;

    @NotNull(message = "pricePerNight isRequired")
    double pricePerNight;

    @NotNull(message = "listingType isRequired")
    String listingTypeId;

    @NotNull(message = "bedroomsCount isRequired")
    int bedroomsCount;

    @NotNull(message = "bathroomsCount isRequired")
    int bathroomsCount;

    @NotNull(message = "guestsCount isRequired")
    int guestsCount;

    List<String> amenities;
    boolean hasBreakfast;
    boolean hasParking;
    boolean isChildFriendly;
    int area;
    List<String> generalRules;
    Instant checkinTime;
    Instant checkoutTime;
}
