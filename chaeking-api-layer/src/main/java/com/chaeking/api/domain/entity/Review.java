package com.chaeking.api.domain.entity;

import lombok.Getter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;

import javax.persistence.*;


@Getter
@DynamicInsert @DynamicUpdate
@Table(name = "review")
@Entity
@Where(clause = "active = 1")
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", foreignKey = @ForeignKey(name = "FK__REVIEW__BOOK"))
    private Book book;

    @Column(length = 10)
    private String type; // 읽고 싶은 책, 읽은 책

    private float rate;

    @Column(length = 300)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

}
