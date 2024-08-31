package com.booking.notificationservice.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
@Data
@Builder  
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppNotificationResponse {
    String id;

    String userId;
    String reservationId;
    
    String content;
    String icon;

    String urlLabel;
    String slug;
}
