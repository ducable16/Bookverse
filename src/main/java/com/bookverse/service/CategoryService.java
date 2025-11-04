package com.bookverse.service;

import com.bookverse.dto.request.CategoryRequest;
import com.bookverse.dto.response.CategoryResponse;

import java.util.List;

public interface CategoryService {

    CategoryResponse create(CategoryRequest request);

    CategoryResponse update(Long id, CategoryRequest request);

    void delete(Long id);

    CategoryResponse getById(Long id);

    CategoryResponse getBySlug(String slug);

    List<CategoryResponse> getAll();
}
