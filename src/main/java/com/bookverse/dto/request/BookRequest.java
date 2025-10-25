package com.bookverse.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class BookRequest {
    private String title;
    private String slug;
    private String coverImage;
    private String description;
    private Integer totalChapters;
    private Long authorId;
    private List<Long> categoryIds;
}
