package com.bookverse.dto.response;

import lombok.Data;

@Data
public class ChapterResponse {
    private Long id;
    private Integer chapterNumber;
    private String title;
    private String content;
}
