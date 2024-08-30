package com.booking.notificationservice.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor 
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotiSettingResponse {
    String userId;
    Boolean receiveBookingApprovalEmail;
    Boolean receiveBookingDenyEmail;
    Boolean receiveBookingConfirmedEmail;
}
