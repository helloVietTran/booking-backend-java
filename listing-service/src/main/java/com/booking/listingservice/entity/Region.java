package com.booking.listingservice.entity;

import org.springframework.data.mongodb.core.mapping.MongoId;

import lombok.*;
import lombok.experimental.FieldDefaults;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE) 
public class Region {
    @MongoId
    String id;
    String imgSrc;
    
    String name;
    String location;// địa điểm trên bản đồ

    @Builder.Default
    Integer totalListings = 0;

    String label;
}
