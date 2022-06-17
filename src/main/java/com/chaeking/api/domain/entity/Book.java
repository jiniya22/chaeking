package com.chaeking.api.domain.entity;

import com.chaeking.api.domain.value.naver.KakaoBookValue;
import com.chaeking.api.domain.value.naver.NaverBookValue;
import com.chaeking.api.util.DateTimeUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

    @Column(length = 500)
    private String link;

    private LocalDate publicationDate;

    @Setter
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "book_id")
    @ToString.Exclude
    private List<BookAndAuthor> bookAndAuthors = new ArrayList<>();

    @Builder
    public Book(String name, int price, String publisher, String isbn, String imageUrl, String link,
                String detailInfo, LocalDate publicationDate) {
        this.name = name;
        this.price = price;
        this.publisher = publisher;
        this.isbn = isbn;
        this.imageUrl = imageUrl;
        this.link = link;
        this.detailInfo = detailInfo;
        this.publicationDate = publicationDate;
    }

    public static Book of(NaverBookValue.Res.BookBasic.Item i) {
        return Book.builder()
                .name(i.getTitle())
                .price(i.getPrice())
                .publisher(i.getPublisher())
                .publicationDate(Optional.ofNullable(i.getPubdate()).filter(f -> f != null && f.length() == 8)
                        .map(m -> LocalDate.parse(m, DateTimeUtils.FORMATTER_DATE_SIMPLE)).orElse(null))
                .isbn(i.getIsbn())
                .imageUrl(i.getImage())
                .link(i.getLink())
                .detailInfo(i.getDescription())
                .build();
    }
    public static Book of(KakaoBookValue.Res.BookBasic.Document d) {
        return Book.builder()
                .name(d.getTitle())
                .price(d.getPrice())
                .publisher(d.getPublisher())
                .publicationDate(Optional.ofNullable(d.getDatetime())
                        .map(m -> LocalDate.parse(m.replaceAll("\\D", "").substring(0,8), DateTimeUtils.FORMATTER_DATE_SIMPLE)).orElse(null))
                .isbn(d.getIsbn())
                .imageUrl(d.getThumbnail())
                .link(d.getUrl())
                .detailInfo(d.getContents())
                .build();
    }

    public void update(NaverBookValue.Res.BookBasic.Item i) {
        this.name = i.getTitle();
        // FIXME update authors
        this.price = i.getPrice();
        this.publisher = i.getPublisher();
        this.publicationDate = Optional.ofNullable(i.getPubdate()).filter(f -> f != null && f.length() == 8)
                .map(m -> LocalDate.parse(m, DateTimeUtils.FORMATTER_DATE_SIMPLE)).orElse(null);
        this.isbn = i.getIsbn();
        this.imageUrl = i.getImage();
        this.link = i.getLink();
        this.detailInfo = i.getDescription();
    }

    public void update(KakaoBookValue.Res.BookBasic.Document i) {
        this.name = i.getTitle();
        // FIXME update authors
        this.price = i.getPrice();
        this.publisher = i.getPublisher();
        this.publicationDate = Optional.ofNullable(i.getDatetime())
                .map(m -> LocalDate.parse(m.replaceAll("\\D", "").substring(0,8), DateTimeUtils.FORMATTER_DATE_SIMPLE)).orElse(null);
        this.isbn = i.getIsbn();
        this.imageUrl = i.getThumbnail();
        this.link = i.getUrl();
        this.detailInfo = i.getContents();
    }
}