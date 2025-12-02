package com.bookverse.dto.response;

import com.bookverse.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class LoginResponse {
    private String token;
    private String username;
    private String email;
    private String fullName;
    private Role role;
}
