package com.booking.profileservice.dto.response;

import java.time.Instant;

import com.booking.profileservice.enums.GenderEnum;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserProfileResponse {
    String userId;
    
    String imgSrc;

    String address;
    String city;
    String postalCode;
    
    String phoneNumber;
    
    Instant dayOfBirth;
    GenderEnum gender;
}
