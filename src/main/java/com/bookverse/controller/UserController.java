package com.bookverse.controller;

import com.bookverse.dto.request.UserRegisterRequest;
import com.bookverse.dto.response.UserResponse;
import com.bookverse.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public UserResponse register(@Valid @RequestBody UserRegisterRequest request) {
        return userService.register(request);
    }

    @GetMapping("/{id}")
    public UserResponse getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }
}
