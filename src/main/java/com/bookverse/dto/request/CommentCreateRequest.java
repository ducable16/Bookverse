package com.bookverse.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

@Getter
@Setter
public class CommentCreateRequest {
    private String content;
    @NotNull
    @NotBlank
    private Long bookId;
    private Long parentId;
}
