package com.booking.identityservice.entity;

import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.ManyToOne;
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
public class ResetPasswordToken {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    /*@ManyToOne// nhiều token co thể tham chiếu đến user
    @JoinColumn(name = "email", referencedColumnName = "email")
    User user;// entity tham chiếu, do jpa tự suy*/
    String email;// để giảm sự phức tạp
    
    String token;

    Instant expiryDate;

    @PrePersist
    public void onPersist(){
        if(expiryDate == null){
            expiryDate = Instant.now().plusSeconds(360);
        }
    }
    
}
