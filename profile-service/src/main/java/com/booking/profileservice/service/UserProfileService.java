package com.booking.profileservice.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.booking.profileservice.dto.request.UpdateAvatarRequest;
import com.booking.profileservice.dto.request.UpdateProfileRequest;
import com.booking.profileservice.dto.request.UserProfileCreationRequest;
import com.booking.profileservice.dto.response.UserProfileResponse;
import com.booking.profileservice.entity.UserProfile;
import com.booking.profileservice.exception.AppException;
import com.booking.profileservice.exception.ErrorCode;
import com.booking.profileservice.mapper.UserProfileMapper;
import com.booking.profileservice.repository.UserProfileRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
//import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
// @Slf4j
public class UserProfileService {
    UserProfileRepository userProfileRepository;
    UserProfileMapper userProfileMapper;

    TokenService tokenService;

    String getUserIdFromToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof Jwt) {
            Jwt jwt = (Jwt) authentication.getPrincipal();
            return jwt.getClaim("id");
        }
        return null;
    }

    public UserProfileResponse createProfile(UserProfileCreationRequest request) {

        UserProfile userProfile = userProfileMapper.toUserProfile(request);
        return userProfileMapper.toUserProfileResponse(userProfileRepository.save(userProfile));
    }

    public UserProfileResponse getProfile(String id) {
        UserProfile userProfile = userProfileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("profile not found"));
        return userProfileMapper.toUserProfileResponse(userProfile);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserProfileResponse> getAllProfiles() {
        return userProfileRepository.findAll().stream().map(userProfileMapper::toUserProfileResponse).toList();
    }

    public void deleteMyProfile(String userId) {
        userProfileRepository.deleteByUserId(userId);
    }

    public void updateAvatar(UpdateAvatarRequest request) {
        Optional<UserProfile> profileOptional = userProfileRepository.findByUserId(request.getUserId());

        if (!profileOptional.isPresent()) {
            throw new AppException(ErrorCode.USER_PROFILE_NOT_EXISTED);
        }

        UserProfile userProfile = profileOptional.get();
        userProfile.setImgSrc(request.getImgSrc());

        userProfileRepository.save(userProfile);
    }

    public void updateProfile(UpdateProfileRequest request) {
        Optional<UserProfile> profileOptional = userProfileRepository.findByUserId(getUserIdFromToken());

        if (!profileOptional.isPresent()) {
            throw new AppException(ErrorCode.USER_PROFILE_NOT_EXISTED);
        }

        UserProfile userProfile = profileOptional.get();

        userProfileMapper.updateUserProfileFromDto(request, userProfile);

        userProfileRepository.save(userProfile);
    }


    public UserProfileResponse getUserProfileByUserId(String userId){
        Optional<UserProfile> profileOptional = userProfileRepository.findByUserId(userId);

        if (!profileOptional.isPresent()) {
            throw new AppException(ErrorCode.USER_PROFILE_NOT_EXISTED);
        }

        UserProfile userProfile = profileOptional.get();

        return userProfileMapper.toUserProfileResponse(userProfile);
    }

    public UserProfileResponse getMyProfile(){
        String userId = tokenService.getUserIdFromToken();
        log.info(userId);
        
        Optional<UserProfile> profileOptional = userProfileRepository.findByUserId(userId);

        if (!profileOptional.isPresent()) {
            throw new AppException(ErrorCode.USER_PROFILE_NOT_EXISTED);
        }

        UserProfile userProfile = profileOptional.get();

        return userProfileMapper.toUserProfileResponse(userProfile);
    }
}
