package xyz.applebox.book.domain.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;

@Getter
@Setter
@Entity
public class Tag extends BaseEntity {

    @Column(length = 50)
    private String name;

}
