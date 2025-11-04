package com.bookverse.service.impl;

import com.bookverse.dto.request.AuthorRequest;
import com.bookverse.dto.response.AuthorResponse;
import com.bookverse.entity.Author;
import com.bookverse.enums.ErrorCode;
import com.bookverse.exception.EntityNotFoundException;
import com.bookverse.repository.AuthorRepository;
import com.bookverse.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    public static Author toEntity(AuthorRequest request) {
        Author author = new Author();
        author.setName(request.getName());
        author.setBiography(request.getBiography());
        author.setAvatarUrl(request.getAvatarUrl());
        return author;
    }

    public static void updateEntity(Author author, AuthorRequest request) {
        author.setName(request.getName());
        author.setBiography(request.getBiography());
        author.setAvatarUrl(request.getAvatarUrl());
    }

    public static AuthorResponse toResponse(Author author) {
        return new AuthorResponse(
                author.getId(),
                author.getName(),
                author.getBiography(),
                author.getAvatarUrl()
        );
    }

    @Override
    public AuthorResponse create(AuthorRequest request) {
        Author author = toEntity(request);
        return toResponse(authorRepository.save(author));
    }

    @Override
    public AuthorResponse update(Long id, AuthorRequest request) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.AUTHOR_NOT_FOUND));

        updateEntity(author, request);

        return toResponse(authorRepository.save(author));
    }

    @Override
    public void delete(Long id) {
        authorRepository.deleteById(id);
    }

    @Override
    public AuthorResponse getById(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.AUTHOR_NOT_FOUND));
        return toResponse(author);
    }

    @Override
    public List<AuthorResponse> getAll() {
        return authorRepository.findAll()
                .stream()
                .map(AuthorServiceImpl::toResponse)
                .toList();
    }
}
