package com.booking.listingservice.entity;

import java.time.Instant;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE) 
public class RecentSearchResult{
    String city;
    String imgSrc;// lưu ảnh, có thể gọi từ region

    int guestsCount;

    Instant checkinDate;
    Instant checkoutDate;
}
