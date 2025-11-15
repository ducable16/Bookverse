package com.bookverse.controller;

import com.bookverse.dto.request.CommentCreateRequest;
import com.bookverse.dto.response.CommentResponse;
import com.bookverse.service.BookCommentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/books/{bookId}/comments")
@AllArgsConstructor
public class BookCommentController {

    private final BookCommentService commentService;

    @PostMapping
    public void createComment(
            @PathVariable Long bookId,
            @RequestBody CommentCreateRequest request
    ) {
        commentService.addComment(bookId, request);
    }

    @GetMapping
    public List<CommentResponse> getComments(
            @PathVariable Long bookId
    ) {
        return commentService.getCommentsByBook(bookId);
    }

    @PutMapping("/{commentId}")
    public void updateComment(
            @PathVariable Long commentId,
            @RequestBody Map<String, String> body
    ) {
        commentService.updateComment(commentId, body.get("content"));
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(
            @PathVariable Long commentId
    ) {
        commentService.deleteComment(commentId);
    }
}

