package com.chaeking.api.domain.entity;

import com.chaeking.api.model.BookMemoryWishValue;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Optional;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "book_memory_wish")
@Entity
@Where(clause = "active = 1")
public class BookMemoryWish extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK__BOOK_MEMORY_WISH__BOOK"))
    private Book book;

    @Column(name = "user_id")
    private long userId;

    @Setter
    @Column(length = 1000)
    private String memo;

    public BookMemoryWish(Book book, long userId) {
        this.book = book;
        this.userId = userId;
    }

    public static BookMemoryWishValue.Res.Simple createSimple(BookMemoryWish w) {
        return new BookMemoryWishValue.Res.Simple(w.getId(),
                Optional.ofNullable(w.getBook()).map(Book::getId).orElse(0L),
                Optional.ofNullable(w.getBook()).map(Book::getName).orElse(""),
                Optional.ofNullable(w.getBook()).map(Book::getImageUrl).orElse(""),
                Optional.ofNullable(w.getBook()).map(Book::getAuthorNames).orElse(""),
                Optional.ofNullable(w.getBook()).map(Book::getPublisherName).orElse(""));
    }

    public static BookMemoryWishValue.Res.Content createContent(BookMemoryWish w) {
        return new BookMemoryWishValue.Res.Content(w.getId(), w.getMemo());
    }
}
