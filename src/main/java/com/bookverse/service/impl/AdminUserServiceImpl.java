package com.bookverse.service.impl;

import com.bookverse.dto.request.AdminUserCreateRequest;
import com.bookverse.dto.request.AdminUserUpdateRequest;
import com.bookverse.dto.request.UserSearchRequest;
import com.bookverse.dto.response.AdminUserResponse;
import com.bookverse.dto.response.UserPageResponse;
import com.bookverse.dto.response.UserStatisticsResponse;
import com.bookverse.entity.User;
import com.bookverse.enums.ErrorCode;
import com.bookverse.enums.Role;
import com.bookverse.exception.AppException;
import com.bookverse.exception.EntityNotFoundException;
import com.bookverse.repository.UserRepository;
import com.bookverse.service.AdminUserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserPageResponse getAllUsers(UserSearchRequest request) {
        Pageable pageable = createPageable(request);
        
        Page<User> userPage = userRepository.searchUsers(
            request.getKeyword(),
            request.getRole(),
            request.getIsActive(),
            request.getIsDeleted(),
            pageable
        );

        List<AdminUserResponse> users = userPage.getContent().stream()
            .map(this::mapToAdminResponse)
            .collect(Collectors.toList());

        return UserPageResponse.builder()
            .users(users)
            .totalUsers(userPage.getTotalElements())
            .currentPage(userPage.getNumber())
            .totalPages(userPage.getTotalPages())
            .build();
    }

    @Override
    public AdminUserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));
        return mapToAdminResponse(user);
    }

    @Override
    @Transactional
    public AdminUserResponse createUser(AdminUserCreateRequest request) {
        // Check username exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USERNAME_ALREADY_EXISTS);
        }

        // Check email exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setRole(request.getRole());
        user.setAvatarUrl(request.getAvatarUrl());
        user.setIsActive(true);
        user.setIsDeleted(false);

        User savedUser = userRepository.save(user);
        return mapToAdminResponse(savedUser);
    }

    @Override
    @Transactional
    public AdminUserResponse updateUser(Long id, AdminUserUpdateRequest request) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));

        // Update username nếu có và khác với current
        if (request.getUsername() != null && !request.getUsername().isEmpty()) {
            if (!user.getUsername().equals(request.getUsername()) &&
                userRepository.existsByUsername(request.getUsername())) {
                throw new AppException(ErrorCode.USERNAME_ALREADY_EXISTS);
            }
            user.setUsername(request.getUsername());
        }

        // Update email nếu có và khác với current
        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            if (!user.getEmail().equals(request.getEmail()) &&
                userRepository.existsByEmail(request.getEmail())) {
                throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);
            }
            user.setEmail(request.getEmail());
        }

        if (request.getFullName() != null) {
            user.setFullName(request.getFullName());
        }

        if (request.getAvatarUrl() != null) {
            user.setAvatarUrl(request.getAvatarUrl());
        }

        User updatedUser = userRepository.save(user);
        return mapToAdminResponse(updatedUser);
    }

    @Override
    @Transactional
    public AdminUserResponse changeUserRole(Long id, Role newRole) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));

        if (user.getIsDeleted()) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }

        user.setRole(newRole);
        User updatedUser = userRepository.save(user);
        return mapToAdminResponse(updatedUser);
    }

    @Override
    @Transactional
    public AdminUserResponse toggleUserStatus(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));

        if (user.getIsDeleted()) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }

        // Toggle isActive
        user.setIsActive(!user.getIsActive());
        User updatedUser = userRepository.save(user);
        return mapToAdminResponse(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));

        // Soft delete
        user.setIsDeleted(true);
        user.setIsActive(false);
        userRepository.save(user);
    }

    @Override
    public UserStatisticsResponse getUserStatistics() {
        Long totalUsers = userRepository.countTotalUsers();
        Long activeUsers = userRepository.countActiveUsers();
        Long blockedUsers = userRepository.countBlockedUsers();
        Long deletedUsers = userRepository.countDeletedUsers();

        // Count by role
        Map<String, Long> usersByRole = new HashMap<>();
        for (Role role : Role.values()) {
            Long count = userRepository.countByRole(role);
            usersByRole.put(role.name(), count);
        }

        return UserStatisticsResponse.builder()
            .totalUsers(totalUsers)
            .activeUsers(activeUsers)
            .blockedUsers(blockedUsers)
            .deletedUsers(deletedUsers)
            .usersByRole(usersByRole)
            .build();
    }

    // Helper methods

    private Pageable createPageable(UserSearchRequest request) {
        int page = request.getPage() != null ? request.getPage() : 0;
        int size = request.getSize() != null ? request.getSize() : 20;
        
        String sortBy = request.getSortBy() != null ? request.getSortBy() : "createdDate";
        String sortDirection = request.getSortDirection() != null ? request.getSortDirection() : "DESC";

        Sort sort = Sort.by(
            "DESC".equalsIgnoreCase(sortDirection) ? Sort.Direction.DESC : Sort.Direction.ASC,
            sortBy
        );

        return PageRequest.of(page, size, sort);
    }

    private AdminUserResponse mapToAdminResponse(User user) {
        return AdminUserResponse.builder()
            .id(user.getId())
            .username(user.getUsername())
            .fullName(user.getFullName())
            .email(user.getEmail())
            .avatarUrl(user.getAvatarUrl())
            .role(user.getRole())
            .isActive(user.getIsActive())
            .isDeleted(user.getIsDeleted())
            .createdDate(user.getCreatedDate())
            .updatedDate(user.getUpdatedDate())
            .createdUser(user.getCreatedUser())
            .updatedUser(user.getUpdatedUser())
            .build();
    }
}
