package com.booking.identityservice.config;

import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.booking.identityservice.dto.request.ProfileCreationRequest;
import com.booking.identityservice.dto.response.ApiResponse;
import com.booking.identityservice.dto.response.AuthenticationResponse;
import com.booking.identityservice.entity.AuthenticationProvider;
import com.booking.identityservice.entity.Role;
import com.booking.identityservice.entity.User;
import com.booking.identityservice.entity.UserAuthentication;
import com.booking.identityservice.exception.AppException;
import com.booking.identityservice.exception.ErrorCode;
import com.booking.identityservice.repository.AuthenticationProviderRepository;
import com.booking.identityservice.repository.RoleRepository;
import com.booking.identityservice.repository.UserAuthenticationRepository;
import com.booking.identityservice.repository.UserRepository;
import com.booking.identityservice.repository.httpclient.ProfileClient;
import com.booking.identityservice.service.TokenProviderService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
    ObjectMapper objectMapper;
    TokenProviderService tokenProviderService;

    UserRepository userRepository;
    RoleRepository roleRepository;
    AuthenticationProviderRepository providerRepository;
    UserAuthenticationRepository userAuthRepository;

    ProfileClient profileClient;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        Optional<User> userOptional = userRepository.findByEmail(email);
        User user;
        if (!userOptional.isPresent()) {
            HashSet<Role> roles = new HashSet<>();
            roleRepository.findById("USER").ifPresent(roles::add);

            user = User.builder()
                    .fullName(name)
                    .email(email)
                    .roles(roles)
                    .isVerified(true)
                    .build();
            user = userRepository.save(user);

            ProfileCreationRequest profileRequest = new ProfileCreationRequest();
            profileRequest.setUserId(user.getId());
            profileClient.createProfile(profileRequest);
    
        } else {
            user = userOptional.get();
        }

        String accessToken = tokenProviderService.generateToken(user, false);
        String refreshToken = tokenProviderService.generateToken(user, true);

        ApiResponse<AuthenticationResponse> apiResponse = ApiResponse.<AuthenticationResponse>builder()
                .result(AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .isAuthenticated(true)
                        .build())
                .build();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));

    }

    // đang thử nghiệm
    User authenticateWithOAuth2(String providerName, String externalId, String email, String name) {

        AuthenticationProvider provider = providerRepository.findById(providerName)
                .orElseThrow(() -> new AppException(ErrorCode.PROVIDER_NOT_FOUND));

        Optional<UserAuthentication> userAuthOptional = userAuthRepository.findByProviderAndExternalId(provider,
                externalId);

        if (userAuthOptional.isPresent()) {
            return userAuthOptional.get().getUser();
        }

        HashSet<Role> roles = new HashSet<>();
        roleRepository.findById("USER").ifPresent(roles::add);

        User newUser = User.builder()
                .email(email)
                .fullName(name)
                .roles(roles)
                .build();

        /*
         * / UserAuthentication userAuth = UserAuthentication.builder()
         * .externalId(externalId)
         * .provider(provider)
         * .user(newUser)
         * .build();
         * newUser.getAuthenticationMethods().add(userAuth);
         */

        userRepository.save(newUser);

        return newUser;
    }

}
