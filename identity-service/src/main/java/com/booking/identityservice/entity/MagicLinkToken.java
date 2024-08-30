package com.booking.identityservice.entity;

import java.time.Instant;

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
public class MagicLinkToken {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String email;
    String token;

    Instant expiryDate;

    @PrePersist
    public void onPersist(){
        if(expiryDate == null){
            expiryDate = Instant.now().plusSeconds(360);
        }
    }
}
