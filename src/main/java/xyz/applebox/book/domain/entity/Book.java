package xyz.applebox.book.domain.entity;

import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class Book extends BaseEntity {

    @Column(nullable = false, length = 150)
    private String title;

    @Column(length = 50)
    private String author;

    private int price;

//    @Column(length = 20)
//    private String publisher;

    @Column(length = 300)
    private String imageUrl;

//    private String status;
}
