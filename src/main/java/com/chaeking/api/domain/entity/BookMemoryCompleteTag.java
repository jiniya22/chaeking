package com.chaeking.api.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class BookMemoryCompleteTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_BOOK_MEMORY_COMPLETE_BOOK_MEMORY_COMPLETE_TAG"))
    private BookMemoryComplete bookMemoryComplete;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_TAG_BOOK_MEMORY_COMPLETE"))
    private Tag tag;

}
