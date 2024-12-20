package com.bookverse.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchResultResponse {
    private Long bookId;
    private String title;
    private String slug;
    private String coverImage;
    private String authorName;
    private List<String> categoryNames;
    private String snippet;              // Highlighted snippet (đoạn trích có highlight từ khóa)
    private String matchType;            // TITLE, AUTHOR, CATEGORY, CONTENT
    private Integer totalChapters;
    private Long matchedChapterId;       // Nếu match từ chapter content
    private String matchedChapterTitle;  // Tiêu đề chapter có match
}
