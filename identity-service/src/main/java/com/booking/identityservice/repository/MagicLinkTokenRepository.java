package com.booking.identityservice.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.booking.identityservice.entity.MagicLinkToken;

public interface MagicLinkTokenRepository extends JpaRepository<MagicLinkToken, String> {
    Optional<MagicLinkToken> findFirstByEmailAndExpiryDateBefore(String email, LocalDateTime now);
}
