package com.bookverse.service;

import com.bookverse.dto.request.AuthorRequest;
import com.bookverse.dto.response.AuthorResponse;

import java.util.List;

public interface AuthorService {

    AuthorResponse create(AuthorRequest request);

    AuthorResponse update(Long id, AuthorRequest request);

    void delete(Long id);

    AuthorResponse getById(Long id);

    List<AuthorResponse> getAll();
}
