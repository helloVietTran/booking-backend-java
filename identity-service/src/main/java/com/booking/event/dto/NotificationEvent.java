package com.booking.event.dto;

import java.util.Map;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationEvent {
    String channel;// thực tế có nhiều kênh để gửi
    String receiver;
    String templateCode;// từ templateCode cần lấy ra nội dung như nào
    Map<String, Object> params;
    String subject;
    String body;
}
