package com.bookverse.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SavedBookResponse {

    private Long id;
    private UserResponse user;
    private BookResponse book;
    private LocalDateTime savedAt;
}
