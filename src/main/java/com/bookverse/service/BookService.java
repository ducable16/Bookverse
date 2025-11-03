package com.bookverse.service;

import com.bookverse.dto.request.BookRequest;
import com.bookverse.dto.response.BookResponse;
import com.bookverse.entity.Book;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookService {

    BookResponse create(BookRequest request);

    BookResponse update(Long id, BookRequest request);

    void delete(Long id);

    BookResponse getById(Long id);

    List<BookResponse> getAll();

    List<BookResponse> getByAuthor(Long authorId, Pageable pageable);

    List<BookResponse> getByCategory(Long categoryId);

    List<BookResponse> searchByTitle(String keyword);

    BookResponse getBySlug(String slug);
}


