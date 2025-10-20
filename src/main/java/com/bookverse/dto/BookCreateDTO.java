package com.bookverse.dto;

import lombok.Data;

import java.util.List;

@Data
public class BookCreateDTO {
    private String title;
    private String slug;
    private String coverImage;
    private String description;
    private Integer totalChapters;
    private Long authorId;
    private List<Long> categoryIds;
}
