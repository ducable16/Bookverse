package com.bookverse.controller;

import com.bookverse.dto.request.BookRequest;
import com.bookverse.dto.response.BookResponse;
import com.bookverse.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/book")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping("/create")
    public ResponseEntity<BookResponse> create(@RequestBody BookRequest request) {
        return ResponseEntity.ok(bookService.create(request));
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<BookResponse> update(@PathVariable Long id, @RequestBody BookRequest request) {
        return ResponseEntity.ok(bookService.update(id, request));
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<BookResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getById(id));
    }

    @GetMapping("/list")
    public ResponseEntity<List<BookResponse>> getAll() {
        return ResponseEntity.ok(bookService.getAll());
    }

    @GetMapping("/author/{authorId}")
    public ResponseEntity<List<BookResponse>> getByAuthor(
            @PathVariable Long authorId,
            @PageableDefault(size = 50) Pageable pageable
    ) {
        return ResponseEntity.ok(bookService.getByAuthor(authorId, pageable));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<BookResponse>> getByCategory(
            @PathVariable Long categoryId
    ) {
        return ResponseEntity.ok(bookService.getByCategory(categoryId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<BookResponse>> search(
            @RequestParam String keyword
    ) {
        return ResponseEntity.ok(bookService.searchByTitle(keyword));
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<BookResponse> getBySlug(
            @PathVariable String slug
    ) {
        return ResponseEntity.ok(bookService.getBySlug(slug));
    }
}


