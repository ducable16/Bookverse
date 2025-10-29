package com.bookverse.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@AllArgsConstructor
public class AuthorResponse {
    private Long id;
    private String name;
    private String biography;
    private String avatarUrl;
}
