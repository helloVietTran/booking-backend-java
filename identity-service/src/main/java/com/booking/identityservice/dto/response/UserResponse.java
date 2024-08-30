package com.booking.identityservice.dto.response;

import java.time.Instant;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {
    String id;
    String email;
    String fullName;
    String displayName;
    Boolean isVerified;

    String formattedLastLoginAt;

    Instant createdAt;
    Instant updatedAt;

    Set<RoleResponse> roles;
}
