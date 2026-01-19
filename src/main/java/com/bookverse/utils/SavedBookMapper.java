package com.bookverse.utils;

import com.bookverse.dto.response.SavedBookResponse;
import com.bookverse.entity.SavedBook;

public class SavedBookMapper {

    public static SavedBookResponse toResponse(SavedBook savedBook) {
        if (savedBook == null) {
            return null;
        }

        SavedBookResponse response = new SavedBookResponse();
        response.setId(savedBook.getId());
        response.setUser(UserMapper.toResponse(savedBook.getUser()));
        response.setBook(BookMapper.toResponse(savedBook.getBook()));
        response.setSavedAt(savedBook.getSavedAt());

        return response;
    }
}
