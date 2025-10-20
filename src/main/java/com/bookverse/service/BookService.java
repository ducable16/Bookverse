package com.bookverse.service;

import com.bookverse.entity.Book;

import java.util.List;

public interface BookService {

    Book create(Book book);

    Book update(Long id, Book book);

    void delete(Long id);

    Book getById(Long id);

    List<Book> getAll();
}
