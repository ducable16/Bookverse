package com.bookverse.service;

import com.bookverse.dto.request.BookSearchRequest;
import com.bookverse.dto.response.SearchPageResponse;

public interface BookSearchService {
    
    /**
     * Search books by title với optional filters
     */
    SearchPageResponse searchByTitle(String keyword, BookSearchRequest filters);
    
    /**
     * Search books by author name với optional filters
     */
    SearchPageResponse searchByAuthor(String keyword, BookSearchRequest filters);
    
    /**
     * Search books in specific category với optional keyword
     */
    SearchPageResponse searchByCategory(Long categoryId, String keyword, BookSearchRequest filters);
    
    /**
     * Full-text search trong chapter content
     */
    SearchPageResponse searchInContent(String keyword, BookSearchRequest filters);
    
    /**
     * Search tất cả (title, author, content) - combined search
     */
    SearchPageResponse searchAll(BookSearchRequest request);
}
