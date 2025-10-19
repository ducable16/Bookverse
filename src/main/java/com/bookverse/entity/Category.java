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
@Table(name = "categories")
public class Category extends BaseEntity {

    private String name;

    @Column(unique = true)
    private String slug;

    @ManyToMany(mappedBy = "categories")
    private List<Book> books = new ArrayList<>();
}
