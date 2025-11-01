package com.bookverse.exception;

import com.bookverse.enums.ErrorCode;

public class UsernameExistsException extends BaseException {
    public UsernameExistsException(String username) {
        super(ErrorCode.USERNAME_EXISTS, "Username already exists: " + username);
    }
}
