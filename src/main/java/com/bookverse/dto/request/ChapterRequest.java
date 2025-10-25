package com.bookverse.dto.request;

import lombok.Data;

@Data
public class ChapterRequest {
    private Integer chapterNumber;
    private String title;
    private String content;
    private Long bookId;
}
