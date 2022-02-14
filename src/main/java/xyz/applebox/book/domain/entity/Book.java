package xyz.applebox.book.domain.entity;

import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class Book extends BaseEntity {

    @Column(nullable = false, length = 150)
    private String name;

    @Column(length = 50)
    private String author;

    private int price;

    @Column(length = 100)
    private String publisher;

    @Column(length = 100)
    private String isbn;

    @Column(length = 300)
    private String imageUrl;

    @Column(columnDefinition = "TEXT")
    private String detailInfo;
//    private String status;
}
