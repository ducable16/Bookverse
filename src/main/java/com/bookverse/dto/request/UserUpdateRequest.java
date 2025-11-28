package com.bookverse.dto.request;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class UserUpdateRequest {
    private String username;
    private String fullName;
    
    @Email(message = "Email should be valid")
    private String email;
    
    private String password;
    private String avatarUrl;
}
