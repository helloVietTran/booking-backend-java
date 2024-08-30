package com.booking.identityservice.repository;

import java.time.Instant;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.booking.identityservice.entity.MagicLinkToken;

@Repository
public interface MagicLinkTokenRepository extends JpaRepository<MagicLinkToken, String> {
    Optional<MagicLinkToken> findFirstByEmailAndExpiryDateBefore(String email, Instant now);
}
