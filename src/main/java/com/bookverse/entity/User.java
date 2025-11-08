package com.bookverse.entity;

import com.bookverse.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_name")
    )
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private List<ReadingHistory> histories = new ArrayList<>();
}

