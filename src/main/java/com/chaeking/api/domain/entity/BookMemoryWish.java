package com.chaeking.api.domain.entity;

import com.chaeking.api.domain.value.BookMemoryWishValue;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class BookMemoryWish extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_BOOK_BOOK_MEMORY_WISH"))
    private Book book;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_USER_BOOK_MEMORY_WISH"))
    private User user;

    @Setter
    @Column(length = 1000)
    private String memo;

    public BookMemoryWish(Book book, User user) {
        this.book = book;
        this.user = user;
    }
}
