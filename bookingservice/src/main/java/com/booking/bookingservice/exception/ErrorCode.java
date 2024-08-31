package com.booking.bookingservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.Getter;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),

    INVALID_KEY(5001, "Uncategorized error", HttpStatus.BAD_REQUEST),
    LISTINGID_REQUIRED(5002, "ListingId is required", HttpStatus.BAD_REQUEST),
    PHONE_NUMBER_REQUIRED(5003, "Phone number is required", HttpStatus.BAD_REQUEST),
    CHECKIN_DATE_REQUIRED(5004, "Checkin date is required", HttpStatus.BAD_REQUEST),
    CHECKOUT_DATE_REQUIRED(5005, "Checkout date is required", HttpStatus.BAD_REQUEST),
    PRICE_REQUIRED(5006, "Total price is required", HttpStatus.BAD_REQUEST),
    NOT_FOUND_LISTING(5007, "Not found listing", HttpStatus.NOT_FOUND),
    NOT_AVAILABLE_LISTING(5008, "This listing is not available", HttpStatus.BAD_REQUEST),
    NOT_FOUND_TOKEN(5009, "Not found listing", HttpStatus.NOT_FOUND),
    INVALID_TOKEN(5010, "Your confirm reservation token is expired or invalid", HttpStatus.BAD_REQUEST),
    NOT_FOUND_RESERVATION(5011, "Not found reservation", HttpStatus.NOT_FOUND),

    UNAUTHENTICATED(5100, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(5101, "You do not have permission", HttpStatus.FORBIDDEN),
    LISTING_SERVICE_ERROR(5102, "Listing service is error", HttpStatus.INTERNAL_SERVER_ERROR),
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