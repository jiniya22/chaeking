package com.chaeking.api.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Table(name = "contact")
@Entity
@Where(clause = "active = 1")
public class Contact extends BaseBoard {

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "FK__CONTACT__USER"))
    private User user;

    @Column(length = 100)
    private String answerTitle;

    @Column(columnDefinition = "TEXT")
    private String answerContent;

}
