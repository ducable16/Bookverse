package com.bookverse.dto.request;

import lombok.Data;

@Data
public class ReadingHistoryRequest {
    private Long userId;
    private Long bookId;
    private Integer lastReadChapter;
}
