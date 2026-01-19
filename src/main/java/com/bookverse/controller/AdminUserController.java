package com.bookverse.controller;

import com.bookverse.dto.request.AdminUserCreateRequest;
import com.bookverse.dto.request.AdminUserUpdateRequest;
import com.bookverse.dto.request.UserSearchRequest;
import com.bookverse.dto.response.AdminUserResponse;
import com.bookverse.dto.response.UserPageResponse;
import com.bookverse.dto.response.UserStatisticsResponse;
import com.bookverse.enums.Role;
import com.bookverse.service.AdminUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")  // Chỉ ADMIN mới access được
public class AdminUserController {

    private final AdminUserService adminUserService;

    /**
     * Lấy danh sách users với search, filter, pagination
     * GET /api/admin/users?keyword=john&role=USER&isActive=true&page=0&size=20
     */
    @GetMapping
    public UserPageResponse getAllUsers(
        @RequestParam(required = false) String keyword,
        @RequestParam(required = false) String role,
        @RequestParam(required = false) Boolean isActive,
        @RequestParam(required = false) Boolean isDeleted,
        @RequestParam(defaultValue = "createdDate") String sortBy,
        @RequestParam(defaultValue = "DESC") String sortDirection,
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "20") Integer size
    ) {
        UserSearchRequest request = UserSearchRequest.builder()
            .keyword(keyword)
            .role(role)
            .isActive(isActive)
            .isDeleted(isDeleted)
            .sortBy(sortBy)
            .sortDirection(sortDirection)
            .page(page)
            .size(size)
            .build();
        
        return adminUserService.getAllUsers(request);
    }

    /**
     * Lấy chi tiết user theo ID
     * GET /api/admin/users/{id}
     */
    @GetMapping("/{id}")
    public AdminUserResponse getUserById(@PathVariable Long id) {
        return adminUserService.getUserById(id);
    }

    /**
     * Tạo user mới (bởi admin)
     * POST /api/admin/users
     */
    @PostMapping
    public AdminUserResponse createUser(@Valid @RequestBody AdminUserCreateRequest request) {
        return adminUserService.createUser(request);
    }

    /**
     * Cập nhật thông tin user
     * PUT /api/admin/users/{id}
     */
    @PutMapping("/{id}")
    public AdminUserResponse updateUser(
        @PathVariable Long id,
        @Valid @RequestBody AdminUserUpdateRequest request
    ) {
        return adminUserService.updateUser(id, request);
    }

    /**
     * Thay đổi role của user
     * PUT /api/admin/users/{id}/role?role=ADMIN
     */
    @PutMapping("/{id}/role")
    public AdminUserResponse changeUserRole(
        @PathVariable Long id,
        @RequestParam Role role
    ) {
        return adminUserService.changeUserRole(id, role);
    }

    /**
     * Block/Unblock user (toggle isActive)
     * PUT /api/admin/users/{id}/toggle-status
     */
    @PutMapping("/{id}/toggle-status")
    public AdminUserResponse toggleUserStatus(@PathVariable Long id) {
        return adminUserService.toggleUserStatus(id);
    }

    /**
     * Soft delete user
     * DELETE /api/admin/users/{id}
     */
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        adminUserService.deleteUser(id);
    }

    /**
     * Lấy thống kê users
     * GET /api/admin/users/statistics
     */
    @GetMapping("/statistics")
    public UserStatisticsResponse getUserStatistics() {
        return adminUserService.getUserStatistics();
    }
}
