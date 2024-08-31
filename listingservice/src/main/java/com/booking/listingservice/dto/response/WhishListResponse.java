package com.booking.listingservice.dto.response;

import java.util.List;


import com.booking.listingservice.entity.Listing;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WhishListResponse {
    String id;

    String userId;
    List<Listing> favouriteList;
}
