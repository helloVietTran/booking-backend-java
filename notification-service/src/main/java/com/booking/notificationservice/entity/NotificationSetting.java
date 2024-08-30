package com.booking.notificationservice.entity;

import java.time.Instant;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Document
@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationSetting {
    @MongoId
    String id;

    @NotNull
    String userId;

    @Builder.Default
    Boolean receiveBookingApprovalEmail = true;
    @Builder.Default
    Boolean receiveBookingDenyEmail = true;
    @Builder.Default
    Boolean receiveBookingConfirmedEmail = true;

    Instant updatedAt;
}
