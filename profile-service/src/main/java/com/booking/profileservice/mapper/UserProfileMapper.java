package com.booking.profileservice.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.booking.profileservice.dto.request.UpdateProfileRequest;
import com.booking.profileservice.dto.request.UserProfileCreationRequest;
import com.booking.profileservice.dto.response.UserProfileResponse;
import com.booking.profileservice.entity.UserProfile;



@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    UserProfile toUserProfile(UserProfileCreationRequest request);
    UserProfileResponse toUserProfileResponse(UserProfile userProfile);
    
    void updateUserProfileFromDto(UpdateProfileRequest request, @MappingTarget UserProfile userProfile);
}
