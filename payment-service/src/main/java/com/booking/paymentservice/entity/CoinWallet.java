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
public class CoinWallet {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String userId;
    
    @Builder.Default
    Integer balance = 1000000;
    
    String rechargeType;
    
    LocalDateTime updatedAt;
    LocalDateTime rechargeDate;// ngày nạp tiền

    @PreUpdate 
    public void onUpdate(){
        updatedAt = LocalDateTime.now();
    }

}
