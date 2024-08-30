package com.booking.identityservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.Getter;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(1999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Uncategorized error", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1002, "User existed", HttpStatus.BAD_REQUEST),
    EMAIL_INVALID(1003, "Invalid email", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1004, "Lenght of password must be between 6 and 20 characters", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, "User not existed", HttpStatus.NOT_FOUND),
    RESET_TOKEN_NOT_EXISTED(1008, "Reset token not existed", HttpStatus.UNAUTHORIZED),
    INVALID_TOKEN(1009, "Your token has beeb expired or wrong ", HttpStatus.BAD_REQUEST),
    PROVIDER_NOT_FOUND(1010, "Provider not found", HttpStatus.NOT_FOUND),

    UNAUTHENTICATED(1900, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1901, "You do not have permission", HttpStatus.FORBIDDEN),
    ;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}