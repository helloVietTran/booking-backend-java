package com.booking.listingservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.Getter;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(3999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    LISTING_NOT_FOUND(3001, "Your listing not found", HttpStatus.NOT_FOUND),
    LISTINGTYPE_NOT_FOUND(3002, "Listing type not found", HttpStatus.NOT_FOUND),
    YOUR_WHISH_LIST_NOT_FOUND(3003, "Your whish list not found", HttpStatus.NOT_FOUND),
    RECENT_SEARCH_NOT_FOUND(3004, "Your whish list not found", HttpStatus.NOT_FOUND),
    REVIEW_NOT_FOUND(3005, "Review not found", HttpStatus.NOT_FOUND),
    REGION_NOT_FOUND(3006, "Region not found", HttpStatus.NOT_FOUND),
    
    UNAUTHENTICATED(3100, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(3101, "You do not have permission", HttpStatus.FORBIDDEN),
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