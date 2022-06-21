package com.chaeking.api.domain.entity;

import com.chaeking.api.domain.value.BookValue;
import com.chaeking.api.util.DateTimeUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
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
        this.isbn = isbn;
        this.imageUrl = imageUrl;
        this.link = link;
        this.detailInfo = detailInfo;
        this.publicationDate = publicationDate;
    }

    public String getAuthorNames() {
        return this.bookAndAuthors.stream().map(m -> m.getAuthor().getName()).collect(Collectors.joining(", "));
    }

    public String getPublisherName() {
        return Optional.ofNullable(this.publisher).map(Publisher::getName).orElse("");
    }

    public static BookValue.Res.Simple createSimple(Book b) {
        return new BookValue.Res.Simple(b.getId(), b.getName(), b.getAuthorNames(), b.getPublisherName());
    }
    public static BookValue.Res.Detail createDetail(Book b) {
        return new BookValue.Res.Detail(b.getId(), b.getName(), b.getPrice(), Optional.ofNullable(b.getPublisher()).map(Publisher::getName).orElse(null),
                DateTimeUtils.toString(b.getPublicationDate()),
                b.getIsbn(), b.getImageUrl(), b.getDetailInfo(),
                b.getBookAndAuthors().stream()
                        .map(m -> Optional.ofNullable(m.getAuthor())
                                .map(Author::getName).orElse(null)).collect(Collectors.toList()));
    }

}