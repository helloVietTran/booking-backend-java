package com.booking.fileservice.repository.httpclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.booking.fileservice.config.AuthenticationRequestInterceptor;
import com.booking.fileservice.dto.request.UpdateAvatarRequest;

@FeignClient(name="profile-service", url ="${app.services.profile}", configuration = {AuthenticationRequestInterceptor.class})
public interface ProfileClient {
    @PatchMapping(value = "/my/avatar", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object updateAvatar(@RequestBody UpdateAvatarRequest request);
}
