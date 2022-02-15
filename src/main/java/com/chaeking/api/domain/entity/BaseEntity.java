package com.chaeking.api.domain.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@Where(clause = "active = 1")
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ColumnDefault("1")
    private boolean active;

    @ColumnDefault("NOW()")
    private LocalDateTime createdAt;

    @Setter
    private LocalDateTime updatedAt;

    @PrePersist
    protected void prePersist() {
        this.active = true;
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected  void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
