package com.chaeking.api.domain.entity;

import com.chaeking.api.domain.enumerate.Sex;
import com.chaeking.api.domain.value.UserValue;
import com.chaeking.api.util.DateTimeUtils;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Optional;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert @DynamicUpdate
@Entity
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false, length = 100)
    private String email;

    @Setter
    @Column(nullable = false, length = 500)
    private String password;

    @Setter
    @Column(nullable = false, length = 100)
    private String name;

    private LocalDate birthDate;

    @Column(length = 1)
    @Enumerated(EnumType.STRING)
    @ColumnDefault("'M'")
    private Sex sex;

    @Builder
    private User(String email, String password, String name, LocalDate birthDate, Sex sex) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.birthDate = birthDate;
        this.sex = sex;
    }
    
    public static User of(UserValue.Req.Creation c) {
        return User.builder()
                .email(c.email())
                .name(c.name())
                .birthDate(Optional.ofNullable(c.birthDate()).map(m -> LocalDate.parse(m, DateTimeUtils.DATE_FORMATTER)).orElse(null))
                .sex(c.sex())
                .password(c.password()).build();
    }
}
