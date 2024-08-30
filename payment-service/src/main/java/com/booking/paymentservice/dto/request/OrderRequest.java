package com.booking.paymentservice.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderRequest {
    String userId;
    
    String reservationId;
    long amount;

    String ownerId;
}
