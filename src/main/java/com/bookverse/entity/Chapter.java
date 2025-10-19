package com.bookverse.entity;

import com.bookverse.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "chapters")
public class Chapter extends BaseEntity {

    private Integer chapterNumber;

    private String title;

    @Column(columnDefinition = "LONGTEXT")
    private String content; // HTML hoặc Markdown

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;
}

