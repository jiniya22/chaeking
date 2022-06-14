package com.chaeking.api.domain.entity;

import com.chaeking.api.domain.value.naver.NaverBookValue;
import com.chaeking.api.util.DateTimeUtils;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

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

    public static Book of(NaverBookValue.Res.BookBasic.Item i) {
        return Book.builder()
                .name(i.getTitle())
                .author(i.getAuthor())
                .price(i.getPrice())
                .publisher(i.getPublisher())
                .publicationDate(Optional.ofNullable(i.getPubdate()).filter(f -> f != null && f.length() == 8)
                        .map(m -> LocalDate.parse(m, DateTimeUtils.FORMATTER_DATE_SIMPLE)).orElse(null))
                .isbn(i.getIsbn())
                .imageUrl(i.getImage())
                .detailInfo(i.getDescription())
                .build();
    }

}