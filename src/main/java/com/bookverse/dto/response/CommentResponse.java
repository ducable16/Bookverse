package com.bookverse.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class CommentResponse {

    private Long id;
    private Long userId;
    private String username;
    private String content;
    private LocalDateTime createdDate;

    private List<CommentResponse> replies;
}

