package com.bookverse.dto.response;

import com.bookverse.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserResponse {
    private Long id;
    private String username;
    private String fullName;
    private String email;
    private String avatarUrl;
    private Role role;
    private Boolean isActive;
    private Boolean isDeleted;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private String createdUser;
    private String updatedUser;
}
