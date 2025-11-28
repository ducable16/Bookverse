package com.bookverse.service;

import com.bookverse.dto.request.UserRegisterRequest;
import com.bookverse.dto.request.UserUpdateRequest;
import com.bookverse.dto.response.UserResponse;
import com.bookverse.entity.User;

public interface UserService {

    UserResponse getUser(Long id);

    Long getUserId(String token);

    User getCurrentUser();

    UserResponse updateUser(Long id, UserUpdateRequest request);
}
