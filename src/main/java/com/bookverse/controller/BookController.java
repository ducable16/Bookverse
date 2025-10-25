package com.bookverse.controller;

import com.bookverse.dto.request.BookRequest;
import com.bookverse.dto.response.BookResponse;
import com.bookverse.entity.Book;
import com.bookverse.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping
    public ResponseEntity<BookResponse> create(@RequestBody BookRequest request) {
        return ResponseEntity.ok(bookService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> update(
            @PathVariable Long id,
            @RequestBody BookRequest request
    ) {
        return ResponseEntity.ok(bookService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<BookResponse>> getAll() {
        return ResponseEntity.ok(bookService.getAll());
    }
}

