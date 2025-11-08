package com.bookverse.service;

import com.bookverse.dto.request.UserRegisterRequest;
import com.bookverse.dto.response.UserResponse;

public interface UserService {

    UserResponse register(UserRegisterRequest request);

    UserResponse getUser(Long id);
}
