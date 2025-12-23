package com.bookverse.service.impl;

import com.bookverse.dto.request.BookRequest;
import com.bookverse.dto.response.BookResponse;
import com.bookverse.entity.Author;
import com.bookverse.entity.Book;
import com.bookverse.entity.Category;
import com.bookverse.enums.ErrorCode;
import com.bookverse.exception.AppException;
import com.bookverse.exception.EntityNotFoundException;
import com.bookverse.repository.AuthorRepository;
import com.bookverse.repository.BookRepository;
import com.bookverse.repository.CategoryRepository;
import com.bookverse.service.BookService;
import com.bookverse.utils.BookMapper;
import com.bookverse.utils.SlugUtil;
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

        String slug = SlugUtil.toSlug(request.getTitle());
        if (bookRepository.existsBySlug(slug)) {
            throw new AppException(ErrorCode.SLUG_ALREADY_EXISTS) {
            };
        }

        Author author = authorRepository.findById(request.getAuthorId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.AUTHOR_NOT_FOUND));

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
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.BOOK_NOT_FOUND));

        String slug = SlugUtil.toSlug(request.getTitle());
        if (bookRepository.existsBySlugAndIdNot(slug, id)) {
            throw new AppException(ErrorCode.SLUG_ALREADY_EXISTS) {
            };
        }

        Author author = authorRepository.findById(request.getAuthorId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.AUTHOR_NOT_FOUND));

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
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.BOOK_NOT_FOUND));
        book.setIsDeleted(true);
        book.setIsActive(false);

        bookRepository.save(book);
    }

    @Override
    @Transactional(readOnly = true)
    public BookResponse getById(Long id) {
        return BookMapper.toResponse(
                bookRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException(ErrorCode.BOOK_NOT_FOUND))
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
        Book book = bookRepository.findBySlug(slug).orElseThrow(() -> new EntityNotFoundException(ErrorCode.BOOK_NOT_FOUND));
        return BookMapper.toResponse(book);
    }
}
