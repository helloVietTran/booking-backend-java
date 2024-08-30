package com.booking.identityservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.booking.identityservice.entity.AuthenticationProvider;

@Repository
public interface AuthenticationProviderRepository extends JpaRepository<AuthenticationProvider, String> {
    
}
