package com.chaeking.api.domain.entity;

import lombok.Getter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;


@Getter
@DynamicInsert @DynamicUpdate
@Entity
public class Review extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK_USER_REVIEW"))
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", foreignKey = @ForeignKey(name = "FK_BOOK_REVIEW"))
    private Book book;

    @Column(length = 10)
    private String type; // 읽고 싶은 책, 읽은 책

    private float rate;

    @Column(length = 300)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

}
