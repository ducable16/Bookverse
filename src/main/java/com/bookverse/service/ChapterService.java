package com.bookverse.service;

import com.bookverse.dto.request.ChapterRequest;
import com.bookverse.dto.response.ChapterResponse;

import java.util.List;

public interface ChapterService {

    ChapterResponse create(ChapterRequest request);

    ChapterResponse update(Long id, ChapterRequest request);

    void delete(Long id);

    ChapterResponse getById(Long id);

    List<ChapterResponse> getByBookId(Long bookId);

    ChapterResponse getByBookIdAndChapterNumber(Long bookId, Integer chapterNumber);
}
