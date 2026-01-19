package com.bookverse.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPageResponse {
    private List<AdminUserResponse> users;
    private Long totalUsers;
    private Integer currentPage;
    private Integer totalPages;
}
