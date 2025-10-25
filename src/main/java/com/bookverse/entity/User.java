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
@Table(name = "users")
public class User extends BaseEntity {

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String email;

    private String password;

    private String avatarUrl;

    @OneToMany(mappedBy = "user")
    private List<ReadingHistory> histories = new ArrayList<>();
}

