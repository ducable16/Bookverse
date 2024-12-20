package com.bookverse.controller;

import com.bookverse.dto.request.BookSearchRequest;
import com.bookverse.dto.response.SearchPageResponse;
import com.bookverse.service.BookSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    private final BookSearchService searchService;

    /**
     * Search books by title
     * Example: GET /api/search/books/by-title?keyword=harry&authorId=1&categoryId=2&sortBy=title&sortDirection=ASC&page=0&size=20
     */
    @GetMapping("/books/by-title")
    public SearchPageResponse searchByTitle(
        @RequestParam String keyword,
        @RequestParam(required = false) Long authorId,
        @RequestParam(required = false) Long categoryId,
        @RequestParam(defaultValue = "title") String sortBy,
        @RequestParam(defaultValue = "ASC") String sortDirection,
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "20") Integer size
    ) {
        BookSearchRequest request = buildRequest(keyword, authorId, categoryId, sortBy, sortDirection, page, size);
        return searchService.searchByTitle(keyword, request);
    }

    /**
     * Search books by author name
     * Example: GET /api/search/books/by-author?keyword=rowling&categoryId=2&sortBy=title&page=0&size=20
     */
    @GetMapping("/books/by-author")
    public SearchPageResponse searchByAuthor(
        @RequestParam String keyword,
        @RequestParam(required = false) Long categoryId,
        @RequestParam(defaultValue = "title") String sortBy,
        @RequestParam(defaultValue = "ASC") String sortDirection,
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "20") Integer size
    ) {
        BookSearchRequest request = buildRequest(keyword, null, categoryId, sortBy, sortDirection, page, size);
        return searchService.searchByAuthor(keyword, request);
    }

    /**
     * Search books in specific category with optional keyword
     * Example: GET /api/search/books/by-category/1?keyword=magic&sortBy=createdAt&sortDirection=DESC&page=0&size=20
     */
    @GetMapping("/books/by-category/{categoryId}")
    public SearchPageResponse searchByCategory(
        @PathVariable Long categoryId,
        @RequestParam(required = false) String keyword,
        @RequestParam(defaultValue = "title") String sortBy,
        @RequestParam(defaultValue = "ASC") String sortDirection,
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "20") Integer size
    ) {
        BookSearchRequest request = buildRequest(keyword, null, categoryId, sortBy, sortDirection, page, size);
        return searchService.searchByCategory(categoryId, keyword, request);
    }

    /**
     * Full-text search in chapter content
     * Example: GET /api/search/books/by-content?keyword=prophecy&authorId=1&categoryId=2&sortBy=relevance&page=0&size=20
     */
    @GetMapping("/books/by-content")
    public SearchPageResponse searchInContent(
        @RequestParam String keyword,
        @RequestParam(required = false) Long authorId,
        @RequestParam(required = false) Long categoryId,
        @RequestParam(defaultValue = "relevance") String sortBy,
        @RequestParam(defaultValue = "DESC") String sortDirection,
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "20") Integer size
    ) {
        BookSearchRequest request = buildRequest(keyword, authorId, categoryId, sortBy, sortDirection, page, size);
        return searchService.searchInContent(keyword, request);
    }

    /**
     * Combined search - search everywhere (title, author, content)
     * Example: GET /api/search/books/all?keyword=magic&authorId=1&categoryId=2&sortBy=relevance&page=0&size=20
     */
    @GetMapping("/books/all")
    public SearchPageResponse searchAll(
        @RequestParam String keyword,
        @RequestParam(required = false) Long authorId,
        @RequestParam(required = false) Long categoryId,
        @RequestParam(defaultValue = "relevance") String sortBy,
        @RequestParam(defaultValue = "DESC") String sortDirection,
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "20") Integer size
    ) {
        BookSearchRequest request = buildRequest(keyword, authorId, categoryId, sortBy, sortDirection, page, size);
        return searchService.searchAll(request);
    }

    // Helper method to build request object
    private BookSearchRequest buildRequest(
        String keyword,
        Long authorId,
        Long categoryId,
        String sortBy,
        String sortDirection,
        Integer page,
        Integer size
    ) {
        return BookSearchRequest.builder()
            .keyword(keyword)
            .authorId(authorId)
            .categoryId(categoryId)
            .sortBy(sortBy)
            .sortDirection(sortDirection)
            .page(page)
            .size(size)
            .build();
    }
}
