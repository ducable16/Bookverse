package com.bookverse.entity.base;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(columnDefinition = "Boolean default true")
    private Boolean isActive = true;

    @Column(columnDefinition = "Boolean default false")
    private Boolean isDeleted = false;

    @Column(updatable = false, name = "created_user")
    @CreatedBy
    private String createdUser;

    @Column(name = "updated_user")
    @LastModifiedBy
    private String updatedUser;

    @CreatedDate
    @Column(updatable = false, name = "created_date")
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;
}

