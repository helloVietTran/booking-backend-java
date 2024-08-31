package com.booking.listingservice.dto.response;

import java.time.Instant;
import java.util.List;

import com.booking.listingservice.entity.Review;
import com.booking.listingservice.enums.ListingStatus;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ListingResponse {
    String id;
    String ownerId;
    
    String title;
    String description;
    String address;
    String city;
    String state;
    double pricePerNight;
    String listingType;
    
    int bedroomsCount;
    int bathroomsCount;
    int guestsCount;

    List<String> amenities;
    List<String> imgSrcs;

    ListingStatus status;

    boolean hasBreakfast;
    boolean hasParking;
    boolean isChildFriendly;

    int area;
    List<String> generalRules;

    Instant checkinTime;
    Instant checkoutTime;

    Double ratingPoint;
    Integer ratingCount;

    List<Review> reviews;
}
