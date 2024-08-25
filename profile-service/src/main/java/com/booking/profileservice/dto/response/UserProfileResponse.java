package com.booking.profileservice.dto.response;


import java.time.LocalDateTime;

import com.booking.profileservice.enums.Gender;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserProfileResponse {
    String userId;
    
    String fullName;
    String displayName;
    String imgSrc;

    String address;
    String city;
    String postalCode;
    
    String phoneNumber;
    
    LocalDateTime dayOfBirth;
    Gender gender;
}
