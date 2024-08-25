package com.booking.bookingservice.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class ConfirmReservationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String reservationId;
    
    String token;

    LocalDateTime expiryDate;

    @PrePersist
    public void onPersist(){
        if(expiryDate == null){
            expiryDate = LocalDateTime.now().plusDays(1);
        }
    }
    
}

