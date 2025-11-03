package com.bookverse.service.impl;

import com.bookverse.dto.request.BookRequest;
import com.bookverse.dto.response.BookResponse;
import com.bookverse.entity.Author;
import com.bookverse.entity.Book;
import com.bookverse.entity.Category;
import com.bookverse.repository.AuthorRepository;
import com.bookverse.repository.BookRepository;
import com.bookverse.repository.CategoryRepository;
import com.bookverse.service.BookService;
import com.bookverse.utils.BookMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public BookResponse create(BookRequest request) {

        String slug = generateSlug(request.getTitle());
        if (bookRepository.existsBySlug(slug)) {
            throw new IllegalArgumentException("Slug already exists: " + slug);
        }

        Author author = authorRepository.findById(request.getAuthorId())
                .orElseThrow(() -> new EntityNotFoundException("Author not found"));

        List<Category> categories = categoryRepository.findAllById(request.getCategoryIds());

        Book book = new Book();
        book.setTitle(request.getTitle());
        book.setSlug(slug);
        book.setDescription(request.getDescription());
        book.setCoverImage(request.getCoverImage());
        book.setAuthor(author);
        book.setCategories(categories);
        book.setTotalChapters(0);

        bookRepository.save(book);

        return BookMapper.toResponse(book);
    }

    @Override
    public BookResponse update(Long id, BookRequest request) {

        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));

        String slug = generateSlug(request.getTitle());
        if (bookRepository.existsBySlugAndIdNot(slug, id)) {
            throw new IllegalArgumentException("Slug already exists: " + slug);
        }

        Author author = authorRepository.findById(request.getAuthorId())
                .orElseThrow(() -> new EntityNotFoundException("Author not found"));

        List<Category> categories = categoryRepository.findAllById(request.getCategoryIds());

        book.setTitle(request.getTitle());
        book.setSlug(slug);
        book.setDescription(request.getDescription());
        book.setCoverImage(request.getCoverImage());
        book.setAuthor(author);
        book.setCategories(categories);

        return BookMapper.toResponse(bookRepository.save(book));
    }

    @Override
    public void delete(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));

        // Soft delete
        book.setIsDeleted(true);
        book.setIsActive(false);

        bookRepository.save(book);
    }

    @Override
    @Transactional(readOnly = true)
    public BookResponse getById(Long id) {
        return BookMapper.toResponse(
                bookRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Book not found"))
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookResponse> getAll() {
        return bookRepository.findAll()
                .stream()
                .map(BookMapper::toResponse)
                .toList();
    }

    private String generateSlug(String title) {
        return title.toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-|-$", "");
    }

    @Override
    public List<BookResponse> getByAuthor(Long authorId, Pageable pageable) {
        return bookRepository.findByAuthorId(authorId, pageable)
                .stream()
                .map(BookMapper::toResponse)
                .toList();
    }

    @Override
    public List<BookResponse> getByCategory(Long categoryId) {
        return bookRepository.findByCategoriesId(categoryId)
                .stream()
                .map(BookMapper::toResponse)
                .toList();
    }

    @Override
    public List<BookResponse> searchByTitle(String keyword) {
        return bookRepository.findByTitleContainingIgnoreCase(keyword)
                .stream()
                .map(BookMapper::toResponse)
                .toList();
    }

    @Override
    public BookResponse getBySlug(String slug) {
        Book book = bookRepository.findBySlug(slug).orElseThrow(() -> new EntityNotFoundException("Book not found"));
        return BookMapper.toResponse(book);
    }
}
