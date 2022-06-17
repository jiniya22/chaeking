package com.chaeking.api.domain.entity;


import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class BookAndAuthor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "FK__BOOK__BOOK_AND_AUTHOR"))
    private Book book;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "FK__AUTHOR__BOOK_AND_AUTHOR"))
    private Author author;

    public BookAndAuthor(Author author) {
        this.author = author;
    }

}
