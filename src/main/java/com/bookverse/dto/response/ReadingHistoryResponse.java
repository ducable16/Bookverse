package com.bookverse.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReadingHistoryResponse {
    private Long id;
    private UserResponse user;
    private BookResponse book;
    private Integer lastReadChapter;
    private LocalDateTime lastReadTime;
}
