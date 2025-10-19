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
@Table(name = "authors")
public class Author extends BaseEntity {

    private String name;

    @Column(columnDefinition = "TEXT")
    private String biography;

    private String avatarUrl;

    @OneToMany(mappedBy = "author")
    private List<Book> books = new ArrayList<>();
}

