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
public class SearchPageResponse {
    private List<SearchResultResponse> results;
    private Long totalResults;
    private Integer currentPage;
    private Integer totalPages;
    private String query;
}
