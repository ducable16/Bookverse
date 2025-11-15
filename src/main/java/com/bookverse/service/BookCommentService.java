package com.bookverse.service;

import com.bookverse.dto.request.CommentCreateRequest;
import com.bookverse.dto.response.CommentResponse;

import java.util.List;

public interface BookCommentService {

    void addComment(Long bookId, CommentCreateRequest request);

    List<CommentResponse> getCommentsByBook(Long bookId);

    void updateComment(Long commentId, String content);

    void deleteComment(Long commentId);
}
