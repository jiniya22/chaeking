package com.chaeking.api.domain.entity;

import com.chaeking.api.domain.value.BookMemoryCompleteValue;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
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
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "book_memory_complete_id")
    private List<BookMemoryCompleteTag> tags = new ArrayList<>();

    public BookMemoryComplete(Book book, User user) {
        this.book = book;
        this.user = user;
    }

    public static BookMemoryCompleteValue.Res.Simple createSimple(BookMemoryComplete c) {
        return new BookMemoryCompleteValue.Res.Simple(c.getId(), Optional.ofNullable(c.getBook()).map(Book::getName).orElse(""));
    }
}
