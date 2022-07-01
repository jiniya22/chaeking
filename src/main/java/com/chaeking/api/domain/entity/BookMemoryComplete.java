package com.chaeking.api.domain.entity;

import com.chaeking.api.domain.value.BookMemoryCompleteValue;
import com.chaeking.api.domain.value.ChaekingProperties;
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

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "FK__BOOK_MEMORY_COMPLETE__USER"))
    private User user;

    @Setter
    @Column(length = 1000)
    private String memo;

    @Setter
    private double rate;

    @Setter
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "book_memory_complete_id")
    private List<BookMemoryCompleteTag> tags = new ArrayList<>();

    public BookMemoryComplete(Book book, User user) {
        this.book = book;
        this.user = user;
    }

    public void addTag(BookMemoryCompleteTag tag) {
        tag.setBookMemoryComplete(this);
        this.tags.add(tag);
    }

    public void removeTags(List<BookMemoryCompleteTag> tags) {
        tags.stream().forEach(tag -> tag.setBookMemoryComplete(null));
        this.tags.removeAll(tags);
    }

    public static BookMemoryCompleteValue.Res.Simple createSimple(BookMemoryComplete c) {
        return new BookMemoryCompleteValue.Res.Simple(c.getId(), Optional.ofNullable(c.getBook()).map(Book::getName).orElse(""));
    }

    public static BookMemoryCompleteValue.Res.Bookshelf createBookshelf(BookMemoryComplete c) {
        return new BookMemoryCompleteValue.Res.Bookshelf(c.getId(),
                Optional.ofNullable(c.getBook()).map(Book::getId).orElse(null),
                Optional.ofNullable(c.getBook()).map(Book::getName).orElse(""),
                c.getMemo(),
                c.getRate(),
                Optional.ofNullable(c.getBook()).map(Book::getImageUrl).orElse(ChaekingProperties.getUrl() + "/static/img/books.png"));
    }

    public static BookMemoryCompleteValue.Res.Content createContent(BookMemoryComplete c) {
        return new BookMemoryCompleteValue.Res.Content(c.getId(), c.getRate(),
                c.getMemo(), c.getTags().stream().map(BookMemoryCompleteTag::getTag).map(Tag::getId).collect(Collectors.toList()));
    }

}
