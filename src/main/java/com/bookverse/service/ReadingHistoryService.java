package com.bookverse.service;

import com.bookverse.dto.request.ReadingHistoryRequest;
import com.bookverse.dto.response.ReadingHistoryResponse;

import java.util.List;

public interface ReadingHistoryService {

    ReadingHistoryResponse save(ReadingHistoryRequest request);

    ReadingHistoryResponse getByUserAndBook(Long userId, Long bookId);

    List<ReadingHistoryResponse> getAllByUser(Long userId);

    void delete(Long id);
}
