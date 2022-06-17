package com.chaeking.api.domain.entity;


import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class BookAndAuthor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @ToString.Exclude
    @JoinColumn(foreignKey = @ForeignKey(name = "FK__BOOK__BOOK_AND_AUTHOR"))
    private Book book;

    @ManyToOne
    @ToString.Exclude
    @JoinColumn(foreignKey = @ForeignKey(name = "FK__AUTHOR__BOOK_AND_AUTHOR"))
    private Author author;

    public static BookAndAuthor of(Book book, Author author) {
        BookAndAuthor bookAndAuthor = new BookAndAuthor();
        bookAndAuthor.setAuthor(author);
        bookAndAuthor.setBook(book);
        return bookAndAuthor;
    }
    public static List<BookAndAuthor> of(Book book, List<Author> authors) {
        return authors.stream().map(author -> of(book, author)).collect(Collectors.toList());
    }

}
