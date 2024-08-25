package com.booking.identityservice.dto.request;


import java.time.LocalDateTime;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfileCreationRequest {
    String userId;
    String fullName;
    String displayName;
    String phoneNumber;
    String city;
    String address;
    LocalDateTime dateOfBirth;
    String gender;
}

