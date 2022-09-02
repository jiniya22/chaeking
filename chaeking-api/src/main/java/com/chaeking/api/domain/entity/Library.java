package com.chaeking.api.domain.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "library")
@Entity
@Where(clause = "active = 1")
public class Library {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 10)
    private String libCode;

    @Column(length = 100)
    private String name;

    private String address;

    @Column(length = 15)
    private String tel;

    private double latitude;

    private double longitude;

    // priavte int BookCount;

}
