package com.booking.fileservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.Getter;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(5999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(5001, "Uncategorized error", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(5002, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(5003, "You do not have permission", HttpStatus.FORBIDDEN),
    FAILED_UPLOAD_FILE(5004, "Upload file failed", HttpStatus.INTERNAL_SERVER_ERROR),
    NO_FILE_UPLOADED(5005, "No file uploaded", HttpStatus.BAD_REQUEST),
    NOT_IMAGE_FILE_TYPE(5006, "Invalid file type. Only image files are allowed", HttpStatus.BAD_REQUEST),
    AVATAR_NOT_FOUND(5007, "You dont have avatar", HttpStatus.BAD_REQUEST)
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