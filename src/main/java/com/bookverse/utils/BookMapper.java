package com.bookverse.utils;

import com.bookverse.dto.response.AuthorResponse;
import com.bookverse.dto.response.BookResponse;
import com.bookverse.dto.response.CategoryResponse;
import com.bookverse.entity.Book;

public class BookMapper {

    public static BookResponse toResponse(Book book) {

        BookResponse dto = new BookResponse();

        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setSlug(book.getSlug());
        dto.setDescription(book.getDescription());
        dto.setCoverImage(book.getCoverImage());
        dto.setTotalChapters(book.getTotalChapters());

        dto.setAuthor(
                AuthorResponse.builder()
                        .id(book.getAuthor().getId())
                        .name(book.getAuthor().getName())
                        .biography(book.getAuthor().getBiography())
                        .build()
        );

        dto.setCategories(
                book.getCategories().stream()
                        .map(cat -> new CategoryResponse(cat.getId(), cat.getName(), cat.getSlug()))
                        .toList()
        );

        return dto;
    }
}
