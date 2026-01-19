package com.bookverse.service;

import com.bookverse.dto.request.AdminUserCreateRequest;
import com.bookverse.dto.request.AdminUserUpdateRequest;
import com.bookverse.dto.request.UserSearchRequest;
import com.bookverse.dto.response.AdminUserResponse;
import com.bookverse.dto.response.UserPageResponse;
import com.bookverse.dto.response.UserStatisticsResponse;
import com.bookverse.enums.Role;

public interface AdminUserService {
    
    /**
     * Lấy danh sách users với search và filter
     */
    UserPageResponse getAllUsers(UserSearchRequest request);
    
    /**
     * Lấy chi tiết user theo ID
     */
    AdminUserResponse getUserById(Long id);
    
    /**
     * Tạo user mới (bởi admin)
     */
    AdminUserResponse createUser(AdminUserCreateRequest request);
    
    /**
     * Cập nhật thông tin user
     */
    AdminUserResponse updateUser(Long id, AdminUserUpdateRequest request);
    
    /**
     * Thay đổi role của user
     */
    AdminUserResponse changeUserRole(Long id, Role newRole);
    
    /**
     * Block/Unblock user (set isActive)
     */
    AdminUserResponse toggleUserStatus(Long id);
    
    /**
     * Soft delete user (set isDeleted = true)
     */
    void deleteUser(Long id);
    
    /**
     * Lấy thống kê users
     */
    UserStatisticsResponse getUserStatistics();
}
