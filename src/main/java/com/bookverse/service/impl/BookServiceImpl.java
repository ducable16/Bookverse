package com.bookverse.service.impl;

import com.bookverse.entity.Book;
import com.bookverse.repository.BookRepository;
import com.bookverse.service.BookService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Override
    public Book create(Book book) {

        if (bookRepository.existsBySlug(book.getSlug())) {
            throw new IllegalArgumentException("Slug already exists: " + book.getSlug());
        }

        return bookRepository.save(book);
    }

    @Override
    public Book update(Long id, Book updatedBook) {
        Book existing = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + id));

        existing.setTitle(updatedBook.getTitle());
        existing.setSlug(updatedBook.getSlug());
        existing.setDescription(updatedBook.getDescription());
        existing.setCoverImage(updatedBook.getCoverImage());
        existing.setAuthor(updatedBook.getAuthor());
        existing.setCategories(updatedBook.getCategories());
        existing.setTotalChapters(updatedBook.getTotalChapters());

        return bookRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
//        Book existing = bookRepository.findById(id)
//                .orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + id));
//
//        existing.setDeleted(true);
//        existing.setActive(false);
//
//        bookRepository.save(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public Book getById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> getAll() {
        return bookRepository.findAll();
    }
}
