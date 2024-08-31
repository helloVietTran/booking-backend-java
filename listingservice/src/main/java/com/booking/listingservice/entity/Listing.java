package com.booking.listingservice.entity;

import java.time.Instant;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import com.booking.listingservice.enums.ListingStatus;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)      
@Document
public class Listing {
    @MongoId    
    String id;
    String ownerId;

    String title;
    String description;
    String address;
    String city;
    double pricePerNight;

    String listingType;
    String listingTypeLabel;

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
    
    @CreatedDate
    Instant createdAt;
    @LastModifiedDate
    Instant updatedAt;

    @Builder.Default
    Double ratingPoint = 0.0;

    @Builder.Default
    Integer ratingCount = 0;

  

    @DBRef
    @Builder.Default
    List<Review> reviewIds = new ArrayList<>();
    
    @Builder.Default
    Integer reviewsCount = 0;

}
