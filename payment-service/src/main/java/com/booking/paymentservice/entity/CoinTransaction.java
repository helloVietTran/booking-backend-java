package com.booking.paymentservice.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class CoinTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String userId;

    String reservationId;

    String amount;
    String transactionType;
    String description;

    LocalDateTime  transactionDate;

    @PrePersist
    public void onCreate() {
        transactionDate = LocalDateTime.now();       
    }
}
