package com.bookverse.dto.request;

import com.bookverse.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSearchRequest {
    private String keyword;          // Search trong username, email, fullName
    private String role;               // Filter theo role
    private Boolean isActive;        // Filter theo status (active/blocked)
    private Boolean isDeleted;       // Filter theo deleted status
    private String sortBy;           // Sort field: username, email, createdDate
    private String sortDirection;    // ASC or DESC
    private Integer page;
    private Integer size;
}
