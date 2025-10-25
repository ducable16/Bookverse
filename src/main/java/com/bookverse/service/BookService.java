package com.bookverse.service;

import com.bookverse.dto.request.BookRequest;
import com.bookverse.dto.response.BookResponse;
import com.bookverse.entity.Book;

import java.util.List;

public interface BookService {

    BookResponse create(BookRequest request);

    BookResponse update(Long id, BookRequest request);

    void delete(Long id);

    BookResponse getById(Long id);

    List<BookResponse> getAll();
}


