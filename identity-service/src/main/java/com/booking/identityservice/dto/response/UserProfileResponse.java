package com.booking.identityservice.dto.response;


import java.time.Instant;

import com.booking.identityservice.enums.GenderEnum;

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
    
    Instant dayOfBirth;
    GenderEnum gender;
}
