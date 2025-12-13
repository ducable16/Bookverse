package com.bookverse.utils;

import com.bookverse.dto.response.ReadingHistoryResponse;
import com.bookverse.entity.ReadingHistory;

public class ReadingHistoryMapper {

    public static ReadingHistoryResponse toResponse(ReadingHistory entity) {
        if (entity == null) {
            return null;
        }

        ReadingHistoryResponse response = new ReadingHistoryResponse();
        response.setId(entity.getId());
        response.setUser(UserMapper.toResponse(entity.getUser()));
        response.setBook(BookMapper.toResponse(entity.getBook()));
        response.setLastReadChapter(entity.getLastReadChapter());
        response.setLastReadTime(entity.getUpdatedDate());

        return response;
    }
}
