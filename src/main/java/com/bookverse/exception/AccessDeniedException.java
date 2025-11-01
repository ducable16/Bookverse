package com.bookverse.exception;

import com.bookverse.enums.ErrorCode;

public class AccessDeniedException extends BaseException {
    public AccessDeniedException(String message) {
        super(ErrorCode.ACCESS_DENIED, message);
    }
}
