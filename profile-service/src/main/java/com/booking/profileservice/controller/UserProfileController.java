package com.booking.profileservice.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.booking.profileservice.dto.ApiResponse;
import com.booking.profileservice.dto.request.UpdateAvatarRequest;
import com.booking.profileservice.dto.request.UpdateProfileRequest;
import com.booking.profileservice.dto.request.UserProfileCreationRequest;
import com.booking.profileservice.dto.response.UserProfileResponse;
import com.booking.profileservice.service.UserProfileService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
//import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
//@Slf4j
public class UserProfileController {

    UserProfileService userProfileService;

    @PostMapping("/new-user-profile")
    UserProfileResponse createProfile(@RequestBody UserProfileCreationRequest request){
        return userProfileService.createProfile(request);
    }

    @GetMapping("/{profileId}")
    UserProfileResponse getUserProfile(@PathVariable String profileId){
        return userProfileService.getProfile(profileId);
    }

    @GetMapping("/all-profile")
    ApiResponse<List<UserProfileResponse>> getAllProfiles(){
        return ApiResponse.<List<UserProfileResponse>>builder()
                        .result(userProfileService.getAllProfiles())
                        .build();
    }

    @DeleteMapping("/my/{userId}")
    ApiResponse<String> deleteMyProfile(@PathVariable String userId){
        userProfileService.deleteMyProfile(userId);
        return ApiResponse.<String>builder().result("My profile has been deleted").build();
    }


    @PutMapping("/my")
    ApiResponse<String> updateProfile(@RequestBody UpdateProfileRequest request){
        userProfileService.updateProfile(request);
        return ApiResponse.<String>builder().result("Update profile sucessfull").build();
    }

    @PatchMapping("/my/avatar")
    ApiResponse<String> updateAvatar(@RequestBody UpdateAvatarRequest request){
        userProfileService.updateAvatar(request);
        return ApiResponse.<String>builder().result("Update avatar sucessfull").build();
    }
    
    @GetMapping("/user/{userId}")
    ApiResponse<UserProfileResponse> getUserProfileByUserId(@PathVariable String userId){

        return ApiResponse.<UserProfileResponse>builder()
                        .result(userProfileService.getUserProfileByUserId(userId))
                        .build();
    }

    @GetMapping("/my-profile")
    ApiResponse<UserProfileResponse> getMyProfile(){

        return ApiResponse.<UserProfileResponse>builder()
                        .result(userProfileService.getMyProfile())
                        .build();
    }
}
