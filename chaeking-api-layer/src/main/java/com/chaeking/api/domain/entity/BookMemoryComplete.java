package com.chaeking.api.domain.entity;

import com.chaeking.api.model.BookMemoryCompleteValue;
import com.chaeking.api.model.ChaekingProperties;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "book_memory_complete", uniqueConstraints = { @UniqueConstraint(name = "UK__BOOK_MEMORY_COMPLETE__USER_ID__BOOK_ID", columnNames = {"user_id", "book_id"}) })
@Entity
@Where(clause = "active = 1")
public class BookMemoryComplete extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK__BOOK_MEMORY_COMPLETE__BOOK"))
    private Book book;

    @Column(name = "user_id")
    private long userId;

    @Setter
    @Column(length = 1000)
    private String memo;

    @Setter
    private double rate;

    @Setter
    @JsonIgnore
    @OneToMany(mappedBy = "bookMemoryComplete", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookMemoryCompleteTag> tags = new ArrayList<>();

    public BookMemoryComplete(Book book, long userId) {
        this.book = book;
        this.userId = userId;
    }

    public void addTag(BookMemoryCompleteTag tag) {
        tag.setBookMemoryComplete(this);
        this.tags.add(tag);
    }

    public void removeTags(List<BookMemoryCompleteTag> tags) {
        tags.forEach(tag -> tag.setBookMemoryComplete(null));
        this.tags.removeAll(tags);
    }

    public static BookMemoryCompleteValue.Res.Simple createSimple(BookMemoryComplete c) {
        return new BookMemoryCompleteValue.Res.Simple(c.getId(),
                Optional.ofNullable(c.getBook()).map(Book::getId).orElse(0L),
                Optional.ofNullable(c.getBook()).map(Book::getName).orElse(""),
                Optional.ofNullable(c.getBook()).map(Book::getImageUrl).orElse(""),
                Optional.ofNullable(c.getBook()).map(Book::getAuthorNames).orElse(""),
                Optional.ofNullable(c.getBook()).map(Book::getPublisherName).orElse(""));
    }

    public static BookMemoryCompleteValue.Res.Bookshelf createBookshelf(BookMemoryComplete c) {
        return new BookMemoryCompleteValue.Res.Bookshelf(c.getId(),
                Optional.ofNullable(c.getBook()).map(Book::getId).orElse(null),
                Optional.ofNullable(c.getBook()).map(Book::getName).orElse(""),
                c.getMemo(),
                c.getRate(),
                Optional.ofNullable(c.getBook()).map(Book::getImageUrl).orElse(ChaekingProperties.getUrl() + "/static/img/book.png"));
    }

    public static BookMemoryCompleteValue.Res.Content createContent(BookMemoryComplete c) {
        return new BookMemoryCompleteValue.Res.Content(c.getId(), c.getRate(),
                c.getMemo(), c.getTags().stream().map(BookMemoryCompleteTag::getTag).map(Tag::getId).collect(Collectors.toList()));
    }

}
