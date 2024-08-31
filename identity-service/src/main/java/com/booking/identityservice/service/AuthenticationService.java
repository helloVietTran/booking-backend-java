package com.booking.identityservice.service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.booking.event.dto.NotificationEvent;
import com.booking.identityservice.dto.request.AuthenticationRequest;
import com.booking.identityservice.dto.request.IntrospectRequest;
import com.booking.identityservice.dto.request.LogoutRequest;
import com.booking.identityservice.dto.request.RefreshRequest;
import com.booking.identityservice.dto.request.SendLinkLoginRequest;
import com.booking.identityservice.dto.request.SendLinkVerifyAccountRequest;
import com.booking.identityservice.dto.request.VerifyAccountLinkRequest;
import com.booking.identityservice.dto.request.VerifyLoginLinkRequest;
import com.booking.identityservice.dto.response.AuthenticationResponse;
import com.booking.identityservice.dto.response.IntrospectResponse;
import com.booking.identityservice.dto.response.UserResponse;
import com.booking.identityservice.entity.DisabledToken;
import com.booking.identityservice.entity.MagicLinkToken;
import com.booking.identityservice.entity.User;
import com.booking.identityservice.entity.VerifyAccountToken;
import com.booking.identityservice.exception.AppException;
import com.booking.identityservice.exception.ErrorCode;
import com.booking.identityservice.mapper.UserMapper;
import com.booking.identityservice.repository.DisabledTokenRepository;
import com.booking.identityservice.repository.MagicLinkTokenRepository;
import com.booking.identityservice.repository.UserRepository;
import com.booking.identityservice.repository.VerifyAccountTokenRepository;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;
    DisabledTokenRepository disabledTokenRepository;
    MagicLinkTokenRepository magicLinkTokenRepository;
    VerifyAccountTokenRepository accountTokenRepository;

    UserMapper userMapper;

    KafkaTemplate<String, Object> kafkaTemplate;
    PasswordEncoder passwordEncoder;

    @NonFinal
    @Value("${app.frontend.magicLinkUri}")
    String magicLinkUri;

    @NonFinal
    @Value("${app.frontend.verifyAccountUri}")
    String verifyAccountUri;

    @NonFinal
    @Value("${jwt.secretKey}")
    protected String SECRET_KEY;

    @NonFinal
    @Value("${jwt.access-duration}")
    protected long ACCESS_DURATION;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    protected long REFRESHABLE_DURATION;

    @NonFinal
    @Value("${app.topic.magicLoginLink}")
    String magicLoginLinkTopic;

    @NonFinal
    @Value("${app.topic.verifyAccountLink}")
    String verifyAccountLinkTopic;

    String getUserIdFromToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof Jwt) {
            Jwt jwt = (Jwt) authentication.getPrincipal();
            return jwt.getClaim("id");
        }
        return null;
    }

    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        var token = request.getAccessToken();

        boolean isValid = true;

        try {
            verifyToken(token, false);
        } catch (AppException e) {
            isValid = false;
        }

        return IntrospectResponse.builder().isValid(isValid).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        boolean isAuthenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!isAuthenticated)
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        var accessToken = generateToken(user, false);
        var refreshToken = generateToken(user, true);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .isAuthenticated(true)
                .build();
    }

    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        try {
            var signAccessToken = verifyToken(request.getAccessToken(), true);
            
            String jid = signAccessToken.getJWTClaimsSet().getJWTID();
            Date expiryTime = signAccessToken.getJWTClaimsSet().getExpirationTime();

            DisabledToken disabledToken = DisabledToken.builder().id(jid).expiryTime(expiryTime).build();

            disabledTokenRepository.save(disabledToken);
        } catch (AppException exception) {
            log.info("Token already expired");
        }
    }

    public AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException {
        var signedJWT = verifyToken(request.getRefreshToken(), true);

        var email = signedJWT.getJWTClaimsSet().getSubject();

        var user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        var accessToken = generateToken(user, false);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .isAuthenticated(true)
                .build();
    }

    public String generateToken(User user, boolean isRefresh) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        Long duration = ACCESS_DURATION;
        if (isRefresh) {
            duration = REFRESHABLE_DURATION;
        }

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issuer("booking.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(duration, ChronoUnit.SECONDS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .claim("id", user.getId())
                .claim("isVerified", user.getIsVerified())
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SECRET_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new RuntimeException(e);
        }
    }

    public void sendLinkLogin(SendLinkLoginRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }

        String token = UUID.randomUUID().toString();
        String hashToken = passwordEncoder.encode(token);

        MagicLinkToken magicLinkToken = MagicLinkToken.builder()
                .email(request.getEmail())
                .token(hashToken)
                .build();
        magicLinkTokenRepository.save(magicLinkToken);

        String loginMagicLink = magicLinkUri + token;

        Map<String, Object> params = new HashMap<>();
        params.put("subject", "Đường dẫn xác minh");
        params.put("loginMagicLink", loginMagicLink);
    
        sendNotificationEvent(magicLoginLinkTopic, params, request.getEmail(), "login-magic-link");
    }

    public AuthenticationResponse verifyLoginLink(VerifyLoginLinkRequest request) {
        Instant now = Instant.now();

        Optional<MagicLinkToken> optionalToken = magicLinkTokenRepository
                .findFirstByEmailAndExpiryDateBefore(request.getEmail(), now);

        if (!optionalToken.isPresent()) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
        MagicLinkToken magicLinkToken = optionalToken.get();

        if (isValidToken(request.getToken(), magicLinkToken.getToken()))
            throw new AppException(ErrorCode.INVALID_TOKEN);

        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());
        if (!optionalUser.isPresent()) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }

        var accessToken = generateToken(optionalUser.get(), false);
        var refreshToken = generateToken(optionalUser.get(), true);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .isAuthenticated(true)
                .build();
    }

    public void sendLinkVerifyAccount(SendLinkVerifyAccountRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }
        String token = UUID.randomUUID().toString();
        String hashToken = passwordEncoder.encode(token);

        VerifyAccountToken verifyAccountToken = VerifyAccountToken.builder()
                .email(request.getEmail())
                .token(hashToken)
                .build();
        accountTokenRepository.save(verifyAccountToken);

        String verifyAccountLink = verifyAccountUri + token;

        Map<String, Object> params = new HashMap<>();
        params.put("subject", "Xác thực tài khoản của bạn");
        params.put("verifyAccountLink", verifyAccountLink);

        sendNotificationEvent(verifyAccountLinkTopic, params, request.getEmail(), "verify-account-link");

    }

    public UserResponse verifyAccountLink(VerifyAccountLinkRequest request) {

        Optional<VerifyAccountToken> optionalToken = accountTokenRepository
                .findFirstByEmailAndExpiryDateBefore(request.getEmail(), Instant.now());

        if (!optionalToken.isPresent()) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
        VerifyAccountToken verifyAccountToken = optionalToken.get();

        if (isValidToken(request.getToken(), verifyAccountToken.getToken()))
            throw new AppException(ErrorCode.INVALID_TOKEN);

        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());
        if (!optionalUser.isPresent()) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }
        User user = optionalUser.get();
        user.setIsVerified(true);

        return userMapper.toUserResponse(userRepository.save(user));
    }

    private SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SECRET_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = (isRefresh)
                ? new Date(signedJWT.getJWTClaimsSet().getIssueTime()
                        .toInstant().plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS).toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();

        var isVerified = signedJWT.verify(verifier);

        if (!(isVerified && expiryTime.after(new Date())))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        if (disabledTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        return signedJWT;
    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");

        if (!CollectionUtils.isEmpty(user.getRoles()))
            user.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_" + role.getName());
                if (!CollectionUtils.isEmpty(role.getPermissions()))
                    role.getPermissions().forEach(permission -> stringJoiner.add(permission.getName()));
            });

        return stringJoiner.toString();
    }

    private void sendNotificationEvent(String topic, Map<String, Object> params, String email, String templateCode) {
        NotificationEvent notificationEvent = NotificationEvent.builder()
                .channel("EMAIL")
                .receiver(email)
                .templateCode(templateCode)
                .params(params)
                .build();

        kafkaTemplate.send(topic, notificationEvent);
    }

    private boolean isValidToken(String token,  String hashToken) {
        return passwordEncoder.matches(token, hashToken);
    }
}