package com.chaeking.api.domain.entity;

import com.chaeking.api.value.enumerate.MetaType;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Getter
@Table(name = "meta")
@Entity
@Where(clause = "active = 1")
public class Meta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private MetaType type;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ColumnDefault("true")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    @Column(columnDefinition = "TINYINT(1)")
    private boolean active;

}
