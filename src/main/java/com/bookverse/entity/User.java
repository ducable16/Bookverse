package com.bookverse.entity;

import com.bookverse.entity.base.BaseEntity;
import com.bookverse.enums.Role;
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

    private String fullName;

    @Column(unique = true)
    private String email;

    private String password;

    private String avatarUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<ReadingHistory> histories = new ArrayList<>();
}
