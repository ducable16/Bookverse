package com.bookverse.entity;

import com.bookverse.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Table(name = "image")
@Builder
@NoArgsConstructor
@Entity
public class Image extends BaseEntity {

    @Column(name = "asset_id", nullable = false)
    private String assetId;

    @Column(name = "owner_id", nullable = false)
    private Long ownerId;

    @Column(name = "url", nullable = false)
    private String url;

    public Image(String assetId, Long userId, String url) {
        this.assetId = assetId;
        this.ownerId = userId;
        this.url = url;
    }
}
