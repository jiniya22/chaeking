package com.chaeking.api.domain.entity;

import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "library", uniqueConstraints = { @UniqueConstraint(name = "UK__LIBRARY__CODE", columnNames = {"code"}) })
@Entity
@Where(clause = "active = 1")
public class Library extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 2)
    private String regionCode;

    @Column(length = 10)
    private String code;

    @Column(length = 100)
    private String name;

    private String address;

    @Column(length = 15)
    private String tel;

    private double latitude;

    private double longitude;

    // priavte int BookCount;

    @Builder
    public Library(String regionCode, String code, String name, String address, String tel, double latitude, double longitude) {
        this.regionCode = regionCode;
        this.code = code;
        this.name = name;
        this.address = address;
        this.tel = tel;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
