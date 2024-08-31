package com.booking.apigateway.service;

import org.springframework.stereotype.Service;

import com.booking.apigateway.dto.ApiResponse;
import com.booking.apigateway.dto.request.IntrospectRequest;
import com.booking.apigateway.dto.response.IntrospectResponse;
import com.booking.apigateway.repository.IdentityClient;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class IdentityService {
    IdentityClient identityClient;

    public Mono<ApiResponse<IntrospectResponse>> introspect(String token){

        return identityClient.introspect(IntrospectRequest.builder()
                                            .accessToken(token)
                                            .build());
    }
}
