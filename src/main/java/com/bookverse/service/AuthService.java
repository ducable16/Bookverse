package com.bookverse.service;

import com.bookverse.dto.request.LoginRequest;
import com.bookverse.dto.request.UserRegisterRequest;
import com.bookverse.dto.response.LoginResponse;
import com.bookverse.dto.response.UserResponse;
import com.bookverse.dto.user.UserDto;
import com.bookverse.entity.User;
import com.bookverse.enums.ErrorCode;
import com.bookverse.enums.Role;
import com.bookverse.exception.AppException;
import com.bookverse.repository.UserRepository;
import com.bookverse.service.impl.UserServiceImpl;
import com.bookverse.utils.CustomUserDetails;
import com.bookverse.utils.JwtService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );
        System.out.println("authentication : " + authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();
        UserDto dto = UserDto.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .build();
        String jwt = jwtService.generateToken(dto);

        return LoginResponse.builder()
                .token(jwt)
                .email(user.getEmail())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .role(user.getRole())
                .build();
    }

    @Transactional
    public UserResponse register(UserRegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USERNAME_ALREADY_EXISTS);
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setIsActive(true);
        user.setRole(Role.USER);  // Gán role mặc định là USER

        User savedUser = userRepository.save(user);

        return UserServiceImpl.mapToResponse(savedUser);
    }
}
