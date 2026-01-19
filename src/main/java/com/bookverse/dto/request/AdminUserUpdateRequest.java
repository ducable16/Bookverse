package com.bookverse.dto.request;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserUpdateRequest {
    
    private String username;
    
    @Email(message = "Email should be valid")
    private String email;
    
    private String fullName;
    
    private String avatarUrl;
}
