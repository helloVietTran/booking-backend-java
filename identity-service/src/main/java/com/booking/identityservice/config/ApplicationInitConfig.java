package com.booking.identityservice.config;

import java.util.HashSet;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.booking.identityservice.dto.request.ProfileCreationRequest;
import com.booking.identityservice.entity.Role;
import com.booking.identityservice.entity.User;
import com.booking.identityservice.repository.RoleRepository;
import com.booking.identityservice.repository.UserRepository;
import com.booking.identityservice.repository.httpclient.ProfileClient;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;

    @NonFinal
    static final String ADMIN_EMAIL = "numberzero0909@gmail.com";

    @NonFinal
    static final String ADMIN_PASSWORD = "admin";

    ProfileClient profileClient;

    @Bean
    @ConditionalOnProperty(
            prefix = "spring",
            value = "datasource.driverClassName",
            havingValue = "com.mysql.cj.jdbc.Driver")
    ApplicationRunner applicationRunner(UserRepository userRepository, RoleRepository roleRepository) {
    
        return args -> {
            if (userRepository.findByEmail(ADMIN_EMAIL).isEmpty()) {

                var roles = new HashSet<Role>();
            
                roleRepository.save(Role.builder()
                    .name("USER")
                    .description("User role")
                    .build());

                Role adminRole = roleRepository.save(Role.builder()
                    .name("ADMIN")
                    .description("Admin role")
                    .build());

            
                roles.add(adminRole);

                User user = User.builder()
                        .email(ADMIN_EMAIL)
                        .password(passwordEncoder.encode(ADMIN_PASSWORD))
                        .roles(roles)
                        .isVerified(true)
                        .build();
                
                    ProfileCreationRequest profileRequest = new ProfileCreationRequest();
                    profileRequest.setUserId(user.getId());
                    profileClient.createProfile(profileRequest);

                userRepository.save(user);

                log.warn("admin user has been created with default password: admin, please change it");
            }
    
        };
    }
}

