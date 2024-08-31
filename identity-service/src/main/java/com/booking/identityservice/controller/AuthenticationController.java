package com.booking.identityservice.controller;

import java.text.ParseException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.booking.identityservice.dto.request.AuthenticationRequest;
import com.booking.identityservice.dto.request.IntrospectRequest;
import com.booking.identityservice.dto.request.LogoutRequest;
import com.booking.identityservice.dto.request.RefreshRequest;
import com.booking.identityservice.dto.request.SendLinkLoginRequest;
import com.booking.identityservice.dto.request.SendLinkVerifyAccountRequest;
import com.booking.identityservice.dto.request.VerifyAccountLinkRequest;
import com.booking.identityservice.dto.request.VerifyLoginLinkRequest;
import com.booking.identityservice.dto.response.ApiResponse;
import com.booking.identityservice.dto.response.AuthenticationResponse;
import com.booking.identityservice.dto.response.IntrospectResponse;
import com.booking.identityservice.dto.response.UserResponse;
import com.booking.identityservice.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/login")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        var result = authenticationService.authenticate(request);
        return ApiResponse.<AuthenticationResponse>builder().result(result).build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> authenticate(@RequestBody IntrospectRequest request)
            throws ParseException, JOSEException {
        var result = authenticationService.introspect(request);
        return ApiResponse.<IntrospectResponse>builder().result(result).build();
    }

    @PostMapping("/refresh")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody RefreshRequest request)
            throws ParseException, JOSEException {
        var result = authenticationService.refreshToken(request);
        return ApiResponse.<AuthenticationResponse>builder().result(result).build();
    }

    @PostMapping("/logout")
    ApiResponse<Void> logout(@RequestBody LogoutRequest request) throws ParseException, JOSEException {
        authenticationService.logout(request);
        return ApiResponse.<Void>builder().build();
    }

    @PostMapping("/send-link-login")
    ApiResponse<Void> sendLinkLogin(@RequestBody SendLinkLoginRequest request) {
        authenticationService.sendLinkLogin(request);
        return ApiResponse.<Void>builder().build();
    }

    @PostMapping("/verify-login-link")
    ApiResponse<AuthenticationResponse> verifyLoginLink(@RequestBody VerifyLoginLinkRequest request) {
        return ApiResponse.<AuthenticationResponse>builder()
                .result(authenticationService.verifyLoginLink(request))
                .build();
    }

    @PostMapping("/send-verify-account-link")
    ApiResponse<String> sendLinkVerifyAccount(@RequestBody SendLinkVerifyAccountRequest request) {
        authenticationService.sendLinkVerifyAccount(request);
        return ApiResponse.<String>builder()
                .result("Verify account link has send")
                .build();

    }

    @PostMapping("/verify-account-link")
    ApiResponse<UserResponse> verifyAccountLink(@RequestBody VerifyAccountLinkRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(authenticationService.verifyAccountLink(request))
                .build();
    }
}