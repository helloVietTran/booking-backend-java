package com.booking.notificationservice.entity;

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
public class AppNotification {
    @MongoId
    String id;

    @NotNull
    String userId;
    String reservationId;// dùng cho thanh toán
    
    String content;
    String icon;// ghi chú, hoặc tags

    String urlLabel;
    String slug;// optional
}
