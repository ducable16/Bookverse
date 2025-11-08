package com.bookverse.entity;

import com.bookverse.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "books")
public class Book extends BaseEntity {

    private String title;

    @Column(unique = true)
    private String slug;

    private String coverImage;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Integer totalChapters;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    @ManyToMany
    @JoinTable(
            name = "book_categories",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories = new ArrayList<>();

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private List<Chapter> chapters = new ArrayList<>();
}
