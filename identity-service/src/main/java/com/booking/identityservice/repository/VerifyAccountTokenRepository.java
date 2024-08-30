package com.booking.identityservice.repository;

import java.time.Instant;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.booking.identityservice.entity.VerifyAccountToken;

@Repository
public interface VerifyAccountTokenRepository extends JpaRepository<VerifyAccountToken, String>{
    Optional<VerifyAccountToken> findFirstByEmailAndExpiryDateBefore(String email, Instant now);
}
