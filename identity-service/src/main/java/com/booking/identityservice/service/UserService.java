package com.booking.identityservice.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.booking.event.dto.NotificationEvent;
import com.booking.identityservice.dto.request.ChangePasswordRequest;
import com.booking.identityservice.dto.request.ProfileCreationRequest;
import com.booking.identityservice.dto.request.UserCreationRequest;
import com.booking.identityservice.dto.request.UserUpdateRequest;
import com.booking.identityservice.dto.response.UserResponse;
import com.booking.identityservice.entity.ResetPasswordToken;
import com.booking.identityservice.entity.Role;
import com.booking.identityservice.entity.User;
import com.booking.identityservice.exception.AppException;
import com.booking.identityservice.exception.ErrorCode;
import com.booking.identityservice.mapper.UserMapper;
import com.booking.identityservice.repository.ResetPasswordTokenRepository;
import com.booking.identityservice.repository.RoleRepository;
import com.booking.identityservice.repository.UserRepository;
import com.booking.identityservice.repository.httpclient.ProfileClient;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    ProfileClient profileClient;
    KafkaTemplate<String, Object> kafkaTemplate;
    ResetPasswordTokenRepository resetPasswordTokenRepository;

    @NonFinal
    @Value("${app.frontend.resetPasswordUri}")
    String resetPasswordUri;

    public UserResponse createUser(UserCreationRequest request) {
        User user = userMapper.toUser(request);

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<Role> roles = new HashSet<>();
        roleRepository.findById("USER").ifPresent(roles::add);

        user.setRoles(roles);

        try {
            user = userRepository.save(user);

            ProfileCreationRequest profileRequest = new ProfileCreationRequest();
            profileRequest.setUserId(user.getId());
            profileClient.createProfile(profileRequest);

            Map<String, Object> params = new HashMap<>();
            params.put("subject", "Welcome to booking.com");
            params.put("createdAt", user.getCreatedAt());

            NotificationEvent notificationEvent = NotificationEvent.builder()
                    .channel("EMAIL")
                    .receiver(request.getEmail())
                    .templateCode("welcome")
                    .params(params)
                    .build();

            // publish kafka
            kafkaTemplate.send("notification-registration", notificationEvent);// nhận 2 đối số topic và data

        } catch (DataIntegrityViolationException exception) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        return userMapper.toUserResponse(user);
    }

    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();
        log.info(email);

        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return userMapper.toUserResponse(user);
    }

    // name có thể là bất kì một trường nào trong User
    @PostAuthorize("returnObject.email == authentication.name") // khi không thỏa mãn thì không return về kết quả
    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        userMapper.updateUser(user, request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Transactional
    public void deleteUser() {
        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();

        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            userRepository.deleteByEmail(email);

            profileClient.deleteMyProfile(user.getId());
        } else {
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getUsers() {
        log.info("In method get Users");
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }

    @PreAuthorize("hasRole('ADMIN')") // trước khi method được gọi
    public UserResponse getUser(String id) {
        return userMapper.toUserResponse(
                userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }

    public void resetPassword(String templateCode) {
        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();

        String token = UUID.randomUUID().toString();
        String hashToken = passwordEncoder.encode(token);

        ResetPasswordToken resetPasswordToken = ResetPasswordToken.builder()
                .token(hashToken)
                .email(email)
                .build();
        resetPasswordTokenRepository.save(resetPasswordToken);

        String resetPasswordLink = resetPasswordUri + token;

        Map<String, Object> params = new HashMap<>();
        params.put("subject", "Yêu cầu thiết lập lại mật khẩu");
        params.put("imageUrl", "");
        params.put("resetLink", resetPasswordLink);

        NotificationEvent notificationEvent = NotificationEvent.builder()
                .channel("EMAIL")
                .receiver(email)
                .params(params)
                .templateCode(templateCode)
                .build();
        kafkaTemplate.send("notification-reset-password-link", notificationEvent);
    }

    @Transactional 
    public void changePassword(ChangePasswordRequest request) {
        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();

        LocalDateTime now = LocalDateTime.now();

        Optional<ResetPasswordToken> optionalToken = resetPasswordTokenRepository.findFirstByEmailAndExpiryDateBefore(email, now);
        if(!optionalToken.isPresent())
            throw new AppException(ErrorCode.RESET_TOKEN_NOT_EXISTED);
        
        ResetPasswordToken resetPasswordToken = optionalToken.get();
            
        if(!isValidResetToken(request.getToken(), resetPasswordToken))
                throw new AppException(ErrorCode.INVALID_TOKEN);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        user.setPassword(passwordEncoder.encode(request.getPassword()));

    }

    private boolean isValidResetToken(String token, ResetPasswordToken resetPasswordToken) {
        //LocalDateTime now = LocalDateTime.now();
        // !resetPasswordToken.getExpiryDate().isBefore(now) &&

        return   passwordEncoder.matches(token, resetPasswordToken.getToken());
    }
}
