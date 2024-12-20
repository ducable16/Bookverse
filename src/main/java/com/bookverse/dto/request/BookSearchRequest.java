package com.bookverse.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookSearchRequest {
    private String keyword;          // Từ khóa search
    private Long authorId;           // Filter theo author
    private Long categoryId;         // Filter theo category
    private String sortBy;           // Sort: title, createdAt, relevance
    private String sortDirection;    // ASC or DESC
    private Integer page;            // Pagination - page number
    private Integer size;            // Page size (số kết quả mỗi trang)
}
