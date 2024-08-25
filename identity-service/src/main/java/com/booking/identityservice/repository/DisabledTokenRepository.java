package com.booking.identityservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.booking.identityservice.entity.DisabledToken;

@Repository
public interface DisabledTokenRepository extends JpaRepository<DisabledToken, String> {
} 
