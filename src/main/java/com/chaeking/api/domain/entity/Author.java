package com.chaeking.api.domain.entity;

import com.chaeking.api.util.BasicUtils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@EntityListeners(value = AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "author", uniqueConstraints = { @UniqueConstraint(name = "UK__AUTHOR__NAME", columnNames = {"name"}) })
@Entity
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 300)
    private String name;

    @Column(nullable = false, length = 300)
    private String simpleName;

    @CreatedDate
    private LocalDateTime createdAt;

    @OneToMany(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "author_id")
    @ToString.Exclude
    private List<BookAndAuthor> bookAndAuthors = new ArrayList<>();

    public Author(String name) {
        this();
        this.name = name;
        this.simpleName = BasicUtils.getSimpleName(name);
    }

}