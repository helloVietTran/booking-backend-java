package com.booking.identityservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.booking.identityservice.entity.AuthenticationProvider;
import com.booking.identityservice.entity.UserAuthentication;

@Repository
public interface UserAuthenticationRepository extends JpaRepository<UserAuthentication, String> {
    Optional<UserAuthentication> 
        findByProviderAndExternalId(AuthenticationProvider provider, String externalId);
}
