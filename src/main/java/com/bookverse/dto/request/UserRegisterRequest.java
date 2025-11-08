package com.bookverse.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegisterRequest {
    @NotBlank(message = "Username can not be blank")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    private String username;

    @NotBlank(message = "Password can not be blank")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    @Email(message = "Email invalid")
    @NotNull
    @NotBlank(message = "Email can not be blank")
    private String email;
}
