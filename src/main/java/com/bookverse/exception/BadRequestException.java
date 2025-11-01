package com.bookverse.exception;

import com.bookverse.enums.ErrorCode;

public class BadRequestException extends BaseException {

    public BadRequestException(String message) {
        super(ErrorCode.INVALID_INPUT, message);
    }
}
