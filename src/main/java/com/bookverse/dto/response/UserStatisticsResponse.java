package com.bookverse.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserStatisticsResponse {
    private Long totalUsers;
    private Long activeUsers;
    private Long blockedUsers;
    private Long deletedUsers;
    private Map<String, Long> usersByRole;  // Role name -> count
}
