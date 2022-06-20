package com.chaeking.api.domain.entity;

import com.chaeking.api.util.BasicUtils;
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
@Table(uniqueConstraints = { @UniqueConstraint(name = "UK__PUBLISHER__NAME", columnNames = {"name"}) })
@Entity
public class Publisher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 300)
    private String name;

    @Column(nullable = false, length = 300)
    private String simpleName;

    @ColumnDefault("NOW()")
    private LocalDateTime createdAt;

    @OneToMany(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "author_id")
    @ToString.Exclude
    private List<Book> books = new ArrayList<>();

    public Publisher(String name) {
        this();
        this.name = name;
        this.simpleName = BasicUtils.getSimpleName(name);
    }

    @PrePersist
    protected void prePersist() {
        this.createdAt = LocalDateTime.now();
    }


}