package com.booking.bookingservice.repository.httpclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.booking.bookingservice.config.AuthenticationRequestInterceptor;
import com.booking.bookingservice.dto.response.UserProfileResponse;

@FeignClient(name = "profile-service", url = "${app.services.profile}", configuration = AuthenticationRequestInterceptor.class)
public interface ProfileClient {
    @GetMapping(value = "/{userId}")
    public UserProfileResponse getListing(@PathVariable String userId);
}
