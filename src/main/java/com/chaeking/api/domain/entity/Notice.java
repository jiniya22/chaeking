package com.chaeking.api.domain.entity;

import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class Notice extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

}
