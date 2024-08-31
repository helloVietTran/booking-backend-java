package com.booking.apigateway.config;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.PathContainer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import com.booking.apigateway.dto.ApiResponse;
import com.booking.apigateway.service.IdentityService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationFilter implements GlobalFilter, Ordered {
    IdentityService identityService;
    ObjectMapper objectMapper;

    @NonFinal
    private String[] publicEndpoint = {
            "/identity/auth/login", "/identity/auth/introspect", "/identity/auth/refresh", "/identity/auth/logout",//ok

            "/identity/auth/send-link-login","/identity/auth/verify-login-link",

            "/identity/users/reset-password", "/identity/users/registration",

           "/listing/10-listing", "/listing/listing-type", "/listing/region",  "/listing/search", "/listing/{listingId}",
           "/listing/region/get-all",

            "/identity/test/home", "/identity/test/user", "/identity/test/profile", "/identity/test/login",
            "/notification/email/send"
    };

    @NonFinal
    @Value("${app.api-prefix}")
    private String apiPrefix;

    @NonFinal
    private List<PathPattern> publicEndpointPatterns;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        if (isPublicEndpoint(exchange.getRequest()))
            return chain.filter(exchange);

        List<String> authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);

        if (CollectionUtils.isEmpty(authHeader))
            return unauthenticated(exchange.getResponse());
            
        // Get token from authorization header
        String token = authHeader.get(0).replace("Bearer", "");

        return identityService.introspect(token).flatMap(introspectResponse -> {
            if (introspectResponse.getResult().isValid())
                return chain.filter(exchange);
            else
                return unauthenticated(exchange.getResponse());
        }).onErrorResume(throwable -> unauthenticated(exchange.getResponse()));
        // các lỗi 500 cũng trả về unauthenticated                                                                        
    }

    @Override
    public int getOrder() {
        return -1;
    }
  

    @PostConstruct
    public void init() {
        PathPatternParser patternParser = new PathPatternParser();
        publicEndpointPatterns = Arrays.stream(publicEndpoint)
                .map(endpoint -> apiPrefix + endpoint)
                .map(patternParser::parse)
                .collect(Collectors.toList());
    }


    private boolean isPublicEndpoint(ServerHttpRequest request) {
        PathContainer path = request.getPath().pathWithinApplication();
        return publicEndpointPatterns.stream().anyMatch(pattern -> pattern.matches(path));
    }

    private Mono<Void> unauthenticated(ServerHttpResponse response) {
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .code(2000)
                .message("Unauthenticated")
                .build();
        String body = null;
        try {
            body = objectMapper.writeValueAsString(apiResponse);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        return response.writeWith(Mono.just(response.bufferFactory().wrap(body.getBytes())));
    }
}
