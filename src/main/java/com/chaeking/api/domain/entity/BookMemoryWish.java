package com.chaeking.api.domain.entity;

import com.chaeking.api.domain.value.BookMemoryWishValue;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Optional;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "book_memory_wish")
@Entity
public class BookMemoryWish extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK__BOOK_MEMORY_WISH__BOOK"))
    private Book book;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK__BOOK_MEMORY_WISH__USER"))
    private User user;

    @Setter
    @Column(length = 1000)
    private String memo;

    public BookMemoryWish(Book book, User user) {
        this.book = book;
        this.user = user;
    }

    public static BookMemoryWishValue.Res.Simple createSimple(BookMemoryWish w) {
        return new BookMemoryWishValue.Res.Simple(w.getId(), Optional.ofNullable(w.getBook()).map(Book::getName).orElse(""));
    }

    public static BookMemoryWishValue.Res.Content createContent(BookMemoryWish w) {
        return new BookMemoryWishValue.Res.Content(w.getId(), w.getMemo());
    }
}
