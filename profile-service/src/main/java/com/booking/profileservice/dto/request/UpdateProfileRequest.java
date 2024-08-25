package com.booking.profileservice.dto.request;

import java.time.LocalDateTime;

import com.booking.profileservice.enums.Gender;

public class UpdateProfileRequest {
    
    String fullName;
    String displayName;
    String imgSrc;

    String address;
    String city;
    String postalCode;
    
    LocalDateTime dayOfBirth;
    Gender gender;
}
