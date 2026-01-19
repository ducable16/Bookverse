package com.bookverse.service;

import com.bookverse.dto.request.SavedBookRequest;
import com.bookverse.dto.response.SavedBookResponse;

import java.util.List;

public interface SavedBookService {

    /**
     * Save a book for a user
     * 
     * @param request SavedBookRequest containing userId and bookId
     * @return SavedBookResponse
     */
    SavedBookResponse saveBook(SavedBookRequest request);

    /**
     * Remove a saved book
     * 
     * @param savedBookId ID of the saved book
     */
    void unsaveBook(Long savedBookId);

    /**
     * Get all saved books for a user
     * 
     * @param userId ID of the user
     * @return List of SavedBookResponse
     */
    List<SavedBookResponse> getUserSavedBooks(Long userId);

    /**
     * Check if a book is saved by a user
     * 
     * @param userId ID of the user
     * @param bookId ID of the book
     * @return true if saved, false otherwise
     */
    boolean isSaved(Long userId, Long bookId);
}
