package com.bookverse.utils;

import com.bookverse.dto.response.ChapterResponse;
import com.bookverse.entity.Chapter;

public class ChapterMapper {

    public static ChapterResponse toResponse(Chapter chapter) {
        ChapterResponse response = new ChapterResponse();

        response.setId(chapter.getId());
        response.setChapterNumber(chapter.getChapterNumber());
        response.setTitle(chapter.getTitle());
        response.setContent(chapter.getContent());

        return response;
    }
}
