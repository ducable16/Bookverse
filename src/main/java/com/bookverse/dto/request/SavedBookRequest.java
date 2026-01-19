package com.bookverse.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SavedBookRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Book ID is required")
    private Long bookId;
}
