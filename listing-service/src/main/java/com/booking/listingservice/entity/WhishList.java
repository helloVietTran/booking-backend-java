package com.booking.listingservice.entity;

import java.time.Instant;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)      
@Document
public class WhishList {
    @MongoId    
    String id;

    String userId;

    @DBRef
    List<Listing> favouriteList;
    
    Instant updatedAt;
}
