package com.booking.bookingservice.dto.response;

import java.time.LocalDateTime;
import java.util.List;

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
public class ListingResponse {
    String id;
    String ownerId;// id người cho thuê để gửi notification tới chủ phòng để xác nhận
    
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

    LocalDateTime checkinTime;
    LocalDateTime checkoutTime;
}
