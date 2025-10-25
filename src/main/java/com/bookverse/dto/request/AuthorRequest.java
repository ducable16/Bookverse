package com.bookverse.dto.request;

import lombok.Data;

@Data
public class AuthorRequest {
    private String name;
    private String biography;
    private String avatarUrl;
}
