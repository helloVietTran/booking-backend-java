package com.booking.paymentservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(2001, "Uncategorized error", HttpStatus.BAD_REQUEST),
    CANNOT_SEND_EMAIL(2002, "Cannot send email", HttpStatus.BAD_REQUEST),
    SETTING_NOT_FOUND(2003, "Notification setting not found", HttpStatus.NOT_FOUND),
    
    UNAUTHENTICATED(2900, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(2901, "You do not have permission", HttpStatus.FORBIDDEN),
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
