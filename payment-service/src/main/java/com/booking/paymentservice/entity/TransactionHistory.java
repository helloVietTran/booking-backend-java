package com.booking.paymentservice.entity;

import java.time.Instant;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class TransactionHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String userId;

    String reservationId;

    String recipientId;

    String amount;// tiền

    String description;

    Instant transactionDate;
    String status;

    String paymentMethod;// chuyển khoản hoặc nhận tiền

    @PrePersist
    public void onCreate() {
        transactionDate = Instant.now();       
    }
}
