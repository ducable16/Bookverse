package com.bookverse.service.impl;

import com.bookverse.dto.request.CommentCreateRequest;
import com.bookverse.dto.response.CommentResponse;
import com.bookverse.entity.Book;
import com.bookverse.entity.BookComment;
import com.bookverse.entity.User;
import com.bookverse.enums.ErrorCode;
import com.bookverse.exception.AppException;
import com.bookverse.repository.BookCommentRepository;
import com.bookverse.repository.BookRepository;
import com.bookverse.repository.UserRepository;
import com.bookverse.service.BookCommentService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class BookCommentServiceImpl implements BookCommentService {

    private final BookRepository bookRepository;
    private final BookCommentRepository commentRepository;
    private final UserRepository userRepository;

    @Override
    public void addComment(Long bookId, CommentCreateRequest request) {

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));

        User user = getCurrentUser();

        BookComment parent = null;
        if (request.getParentId() != null) {
            parent = commentRepository.findById(request.getParentId())
                    .orElseThrow(() -> new AppException(ErrorCode.PARENT_COMMENT_NOT_FOUND));
        }

        BookComment comment = new BookComment();
        comment.setBook(book);
        comment.setUser(user);
        comment.setParent(parent);
        comment.setContent(request.getContent());

        commentRepository.save(comment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentResponse> getCommentsByBook(Long bookId) {

        List<BookComment> rootComments =
                commentRepository.findByBookIdAndParentIsNullAndIsDeletedFalseOrderByCreatedDateDesc(bookId);

        return rootComments.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public void updateComment(Long commentId, String content) {

        BookComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));

        if (!comment.getUser().getId().equals(getCurrentUser().getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED, "You don't have permission to edit this comment");
        }

        comment.setContent(content);
    }

    @Override
    public void deleteComment(Long commentId) {

        BookComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));

        if (!comment.getUser().getId().equals(getCurrentUser().getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED, "You don't have permission to delete this comment");
        }

        comment.setIsDeleted(true);
        comment.setContent("Comment deleted");
    }

    private CommentResponse mapToResponse(BookComment comment) {

        List<BookComment> replies =
                commentRepository.findByParentIdAndIsDeletedFalseOrderByCreatedDateAsc(comment.getId());

        return CommentResponse.builder()
                .id(comment.getId())
                .userId(comment.getUser().getId())
                .username(comment.getUser().getUsername())
                .content(comment.getContent())
                .createdDate(comment.getCreatedDate())
                .replies(
                        replies.stream()
                                .map(this::mapToResponse)
                                .toList()
                )
                .build();
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }
}

