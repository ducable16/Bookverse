package com.bookverse.controller;

import com.bookverse.dto.request.BookRequest;
import com.bookverse.dto.response.BookResponse;
import com.bookverse.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/book")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping("/create")
    public BookResponse create(@RequestBody BookRequest request) {
        return bookService.create(request);
    }

    @PostMapping("/update/{id}")
    public BookResponse update(@PathVariable Long id, @RequestBody BookRequest request) {
        return bookService.update(id, request);
    }

    @PostMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        bookService.delete(id);
    }

    @GetMapping("/detail/{id}")
    public BookResponse getById(@PathVariable Long id) {
        return bookService.getById(id);
    }

    @GetMapping("/list")
    public List<BookResponse> getAll() {
        return bookService.getAll();
    }

    @GetMapping("/author/{authorId}")
    public List<BookResponse> getByAuthor(
            @PathVariable Long authorId,
            @PageableDefault(size = 50) Pageable pageable
    ) {
        return bookService.getByAuthor(authorId, pageable);
    }

    @GetMapping("/category/{categoryId}")
    public List<BookResponse> getByCategory(@PathVariable Long categoryId) {
        return bookService.getByCategory(categoryId);
    }

    @GetMapping("/search")
    public List<BookResponse> search(@RequestParam String keyword) {
        return bookService.searchByTitle(keyword);
    }

    @GetMapping("/slug/{slug}")
    public BookResponse getBySlug(@PathVariable String slug) {
        return bookService.getBySlug(slug);
    }
}

