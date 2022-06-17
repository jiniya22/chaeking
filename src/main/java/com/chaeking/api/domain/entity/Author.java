package com.chaeking.api.domain.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = { @UniqueConstraint(name = "UK__AUTHOR__NAME", columnNames = {"name"}) })
@Entity
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 300)
    private String name;

    @Column(nullable = false, length = 300)
    private String simpleName;

    @ColumnDefault("NOW()")
    private LocalDateTime createdAt;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "author_id")
    @ToString.Exclude
    private List<BookAndAuthor> bookAndAuthors = new ArrayList<>();

    public Author(String name) {
        this();
        this.name = name;
        this.simpleName = getSimpleName(name);
    }

    @PrePersist
    protected void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    String getSimpleName(String name) {
        String simpleName = name.replaceAll("[^\\da-zA-Z가-힣]", "");
        return simpleName;
    }
}