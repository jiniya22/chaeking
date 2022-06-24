package com.chaeking.api.domain.entity;

import com.chaeking.api.domain.value.BookValue;
import com.chaeking.api.domain.value.ChaekingProperties;
import com.chaeking.api.util.DateTimeUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.apache.logging.log4j.util.Strings;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Column(length = 13)
    private String isbn10;

    @Column(length = 15)
    private String isbn13;

    @Column(length = 300)
    private String imageUrl;

    @Column(columnDefinition = "TEXT")
    private String detailInfo;

//    private String status;

    @Column(length = 500)
    private String link;

    private LocalDate publicationDate;

    @Setter
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK__BOOK__PUBLISHER"))
    private Publisher publisher;

    @Setter
    @OneToMany(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "book_id")
    @ToString.Exclude
    @Where(clause = "author_id IS NOT NULL")
    private List<BookAndAuthor> bookAndAuthors = new ArrayList<>();

    @Builder
    public Book(String name, int price, String isbn, String imageUrl, String link,
                String detailInfo, LocalDate publicationDate) {
        this.name = name;
        this.price = price;
        this.setIsbn(isbn);
        this.imageUrl = imageUrl;
        this.link = link;
        this.detailInfo = detailInfo;
        this.publicationDate = publicationDate;
    }

    private void setIsbn(String isbn) {
        if(Strings.isBlank(isbn))   return;
        Arrays.stream(isbn.trim().split(" ")).forEach(f -> {
            if(f.length() == 10) {
                this.isbn10 = f;
            } else if (f.length() == 13) {
                this.isbn13 = f;
            }
        });
    }

    public String getAuthorNames() {
        return this.bookAndAuthors.stream().map(m -> m.getAuthor().getName()).collect(Collectors.joining(", "));
    }

    public String getPublisherName() {
        return Optional.ofNullable(this.publisher).map(Publisher::getName).orElse("");
    }

    public static BookValue.Res.Simple createSimple(Book b) {
        return new BookValue.Res.Simple(b.getId(), b.getName(), b.getAuthorNames(), b.getPublisherName(), b.getImageUrl());
    }
    public static BookValue.Res.Detail createDetail(Book b) {
        String isbn = b.getIsbn10().isEmpty() ? b.getIsbn13() : String.format("%s(%s)", b.getIsbn13(), b.getIsbn10());
        return new BookValue.Res.Detail(b.getId(), b.getName(), b.getPrice(), Optional.ofNullable(b.getPublisher()).map(Publisher::getName).orElse(null),
                DateTimeUtils.toString(b.getPublicationDate()),
                isbn, b.getImageUrl(), b.getDetailInfo(),
                b.getBookAndAuthors().stream()
                        .map(m -> Optional.ofNullable(m.getAuthor())
                                .map(Author::getName).orElse(null)).collect(Collectors.toList()));
    }

    public String getImageUrl() {
        return Strings.isBlank(this.imageUrl) ? ChaekingProperties.getUrl() + "/static/img/books.png" : this.imageUrl;
    }
}