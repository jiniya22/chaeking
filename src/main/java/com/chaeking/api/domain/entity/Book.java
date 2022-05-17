package com.chaeking.api.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Book extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    private LocalDate publicationDate;

    @Builder
    public Book(String name, String author, int price, String publisher, String isbn, String imageUrl,
                String detailInfo, LocalDate publicationDate) {
        this.name = name;
        this.author = author;
        this.price = price;
        this.publisher = publisher;
        this.isbn = isbn;
        this.imageUrl = imageUrl;
        this.detailInfo = detailInfo;
        this.publicationDate = publicationDate;
    }
}