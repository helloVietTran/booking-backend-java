package com.booking.listingservice.entity;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
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
public class Review {
    @MongoId    
    String id;

    String userId;
    String listingId;
   
    String content;
    Integer ratingPoint;
    
    @CreatedDate
    Instant createdAt;
    @LastModifiedDate
    Instant updatedAt;
}
