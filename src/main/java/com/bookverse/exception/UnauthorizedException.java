package com.bookverse.exception;

import com.bookverse.enums.ErrorCode;

public class UnauthorizedException extends BaseException {

    public UnauthorizedException() {
        super(ErrorCode.UNAUTHORIZED, "Not authorized");
    }
}
