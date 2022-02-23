package com.chaeking.api.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
public class BookMemoryComplete extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_BOOK_BOOK_MEMORY_COMPLETE"))
    private Book book;

    @Column(length = 1000)
    private String memo;

    private int heart;

    @JsonIgnore
    @OneToMany
    @JoinColumn(name = "book_memory_complete_id")
    private List<BookMemoryCompleteTag> tags = new ArrayList<>();

}
