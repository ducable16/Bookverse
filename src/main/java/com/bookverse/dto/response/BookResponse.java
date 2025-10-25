package com.bookverse.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class BookResponse {
    private Long id;
    private String title;
    private String slug;
    private String coverImage;
    private String description;
    private Integer totalChapters;

    private AuthorResponse author;
    private List<CategoryResponse> categories;
}
