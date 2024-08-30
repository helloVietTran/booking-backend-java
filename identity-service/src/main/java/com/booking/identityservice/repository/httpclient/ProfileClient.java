package com.booking.identityservice.repository.httpclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;

import com.booking.identityservice.config.AuthenticationRequestInterceptor;
import com.booking.identityservice.dto.request.ProfileCreationRequest;
import com.booking.identityservice.dto.response.UserProfileResponse;

@FeignClient(name="profile-service", url ="${app.services.profile}", configuration = {AuthenticationRequestInterceptor.class})
public interface ProfileClient {
    @PostMapping(value = "/new-user-profile", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserProfileResponse createProfile(@RequestBody ProfileCreationRequest request);

    @DeleteMapping(value = "/my/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteMyProfile(@PathVariable String userId);

}
