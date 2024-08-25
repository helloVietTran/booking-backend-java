package com.booking.paymentservice.dto.response;


import java.time.LocalDateTime;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WalletResponse {
    String id;
    Integer balance;
    LocalDateTime updatedAt;
}
