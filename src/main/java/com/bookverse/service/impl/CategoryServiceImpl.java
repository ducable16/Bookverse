package com.bookverse.service.impl;

import com.bookverse.dto.request.CategoryRequest;
import com.bookverse.dto.response.CategoryResponse;
import com.bookverse.entity.Category;
import com.bookverse.enums.ErrorCode;
import com.bookverse.exception.AppException;
import com.bookverse.exception.EntityNotFoundException;
import com.bookverse.repository.CategoryRepository;
import com.bookverse.service.CategoryService;
import com.bookverse.utils.SlugUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public static Category toEntity(CategoryRequest request) {
        Category category = new Category();
        category.setName(request.getName());
        return category;
    }

    public static CategoryResponse toResponse(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getSlug()
        );
    }

    @Override
    public CategoryResponse create(CategoryRequest request) {

        String slug = SlugUtil.toSlug(request.getName());

        if (categoryRepository.existsBySlug(slug)) {
            throw new AppException(ErrorCode.CATEGORY_ALREADY_EXISTS) {
            };
        }

        Category category = toEntity(request);
        category.setSlug(slug);
        return toResponse(categoryRepository.save(category));
    }

    @Override
    public CategoryResponse update(Long id, CategoryRequest request) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.CATEGORY_NOT_FOUND));

        category.setName(request.getName());
        String slug = SlugUtil.toSlug(request.getName());
        category.setSlug(slug);

        return toResponse(categoryRepository.save(category));
    }

    @Override
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public CategoryResponse getById(Long id) {
        return toResponse(
                categoryRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException(ErrorCode.CATEGORY_NOT_FOUND))
        );
    }

    @Override
    public CategoryResponse getBySlug(String slug) {
        return toResponse(
                categoryRepository.findBySlug(slug)
                        .orElseThrow(() -> new EntityNotFoundException(ErrorCode.CATEGORY_NOT_FOUND))
        );
    }

    @Override
    public List<CategoryResponse> getAll() {
        return categoryRepository.findAll()
                .stream()
                .map(CategoryServiceImpl::toResponse)
                .toList();
    }
}
