package com.bookverse.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {

    INTERNAL_SERVER_ERROR(500, "Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR),

    // --- 9xxx: System & Defaults ---
    URL_NOT_FOUND(9998, "URL not found", HttpStatus.INTERNAL_SERVER_ERROR),
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Invalid error key", HttpStatus.BAD_REQUEST),

    // --- 1xxx: User & Auth ---
    USER_EXISTED(1002, "User already exists", HttpStatus.CONFLICT),
    USERNAME_INVALID(1003, "Username must be at least 3 characters", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1004, "Password must be at least 8 characters", HttpStatus.BAD_REQUEST),
    WRONG_PASSWORD(1005, "Incorrect password", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1006, "User not found", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1007, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1008, "You do not have permission", HttpStatus.FORBIDDEN),
    INVALID_OTP(1009, "Invalid or expired OTP", HttpStatus.BAD_REQUEST),
    INVALID_INPUT(1010, "Invalid input", HttpStatus.BAD_REQUEST),
    INVALID_FORMAT(1011, "Invalid JSON format", HttpStatus.BAD_REQUEST),

    // --- 2xxx: Book/Domain ---
    BOOK_NOT_FOUND(2001, "Book not found", HttpStatus.NOT_FOUND),
    CATEGORY_NOT_FOUND(2002, "Category not found", HttpStatus.NOT_FOUND),
    AUTHOR_NOT_FOUND(2003, "Author not found", HttpStatus.NOT_FOUND),
    CHAPTER_NOT_FOUND(204, "Chapter not found", HttpStatus.NOT_FOUND),
    BOOK_ALREADY_EXISTS(2011, "Book already exists", HttpStatus.CONFLICT),
    CATEGORY_ALREADY_EXISTS(2012, "Category already exists", HttpStatus.CONFLICT),
    SLUG_ALREADY_EXISTS(2013, "Slug already exists", HttpStatus.CONFLICT),

    ;

    private final int code;
    private final String message;
    private final HttpStatusCode httpStatus;

    ErrorCode(int code, String message, HttpStatusCode httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}