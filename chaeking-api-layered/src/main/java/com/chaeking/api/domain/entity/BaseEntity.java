package com.chaeking.api.domain.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import java.time.LocalDateTime;

@EntityListeners(value = AuditingEntityListener.class)
@Getter
@MappedSuperclass
@DynamicInsert
@DynamicUpdate
public abstract class BaseEntity {

    @Setter
    @ColumnDefault("true")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    @Column(columnDefinition = "TINYINT(1)")
    private boolean active;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @PrePersist
    protected void prePersist() {
        this.active = true;
    }

}
