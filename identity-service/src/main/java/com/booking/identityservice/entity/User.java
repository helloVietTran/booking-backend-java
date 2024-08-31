package com.booking.identityservice.entity;

import java.time.Instant;
import java.util.Set;

import com.booking.identityservice.enums.StatusEnum;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(unique = true)
    String email;
    String password;
    
    String fullName;
    String displayName;

    @Builder.Default
    Boolean isVerified = false;
    
    @ManyToMany
    Set<Role> roles;
    
    @Builder.Default
    StatusEnum status = StatusEnum.INACTIVE;

    Instant lastLogin;
    Instant createdAt;
    Instant updatedAt;

    @PrePersist
    public void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
        lastLogin = Instant.now();
    }
    @PreUpdate 
    public void onUpdate(){
        updatedAt = Instant.now();
    }

    /*  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    Set<UserAuthentication> authenticationMethods = new HashSet<>();*/
}

