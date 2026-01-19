package com.bookverse.controller;

import com.bookverse.dto.request.SavedBookRequest;
import com.bookverse.dto.response.ApiResponse;
import com.bookverse.dto.response.SavedBookResponse;
import com.bookverse.service.SavedBookService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/saved-books")
@AllArgsConstructor
public class SavedBookController {

    private final SavedBookService savedBookService;

    /**
     * Save a book for a user
     * POST /api/saved-books/save
     */
    @PostMapping("/save")
    public SavedBookResponse saveBook(@Valid @RequestBody SavedBookRequest request) {
        return savedBookService.saveBook(request);
    }

    /**
     * Remove a saved book
     * POST /api/saved-books/unsave?savedBookId=1
     */
    @PostMapping("/unsave")
    public String unsaveBook(@RequestParam Long savedBookId) {
        savedBookService.unsaveBook(savedBookId);
        return "Book unsaved successfully";
    }

    /**
     * Get all saved books for a user
     * GET /api/saved-books/user/{userId}
     */
    @GetMapping("/user/{userId}")
    public List<SavedBookResponse> getUserSavedBooks(@PathVariable Long userId) {
        return savedBookService.getUserSavedBooks(userId);
    }

    /**
     * Check if a book is saved by a user
     * GET /api/saved-books/check?userId=1&bookId=5
     */
    @GetMapping("/check")
    public boolean checkSaved(@RequestParam Long userId, @RequestParam Long bookId) {
        return savedBookService.isSaved(userId, bookId);
    }
}
