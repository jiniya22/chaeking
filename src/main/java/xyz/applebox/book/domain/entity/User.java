package xyz.applebox.book.domain.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import xyz.applebox.book.domain.enumerate.Sex;

import javax.persistence.*;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert @DynamicUpdate
@Entity
public class User extends BaseEntity {

    @Setter
    @Column(nullable = false, length = 100)
    private String email;

    @Setter
    @Column(nullable = false, length = 500)
    private String password;

    @Setter
    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 8)
    private String birthDate;

    @Column(length = 1)
    @Enumerated(EnumType.STRING)
    @ColumnDefault("'M'")
    private Sex sex;

    @Builder
    public User(String email, String password, String name, String birthDate, Sex sex) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.birthDate = birthDate;
        this.sex = sex;
    }
}
