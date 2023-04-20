package com.chaeking.api.user.adapter.out.persistence;

import com.chaeking.api.common.BaseEntity;
import com.chaeking.api.common.enumerate.Sex;
import com.chaeking.api.user.domain.User;
import lombok.*;
import org.hibernate.annotations.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Table;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@ToString
@Getter
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "`user`")
@Entity
@Where(clause = "active = 1")
class UserEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false, length = 100)
    private String email;

    @Setter
    @Column(nullable = false, length = 100)
    private String nickname;

    @Column(length = 1)
    @Enumerated(EnumType.STRING)
    @ColumnDefault("'M'")
    private Sex sex;

    @Setter
    @Column(nullable = false, length = 200)
    private String secretKey;

    @Setter
    @Column(length = 500)
    private String imageUrl;

    @Setter
    @ColumnDefault("false")
    @Type(type = "org.hibernate.type.NumericBooleanType")
//    @Column(columnDefinition = "TINYINT(1)")
    private boolean push;

    @Setter
    @ColumnDefault("false")
    @Type(type = "org.hibernate.type.NumericBooleanType")
//    @Column(columnDefinition = "TINYINT(1)")
    private boolean nightPush;

    @Setter
    @Column(length = 40)
    private String refreshKey;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK__USER__USER_AUTHORITY"))
    private Set<UserAuthorityEntity> authorities;

    @Builder
    private UserEntity(String email, String nickname, Sex sex, String secretKey, boolean push,
                       boolean nightPush, String imageUrl) {
        this.email = email;
        this.nickname = nickname;
        this.sex = sex;
        this.secretKey = secretKey;
        this.push = push;
        this.nightPush = nightPush;
        this.imageUrl = imageUrl;
    }

    public void initializeAuthorities() {
        Set<UserAuthorityEntity> authorities = new HashSet<>();
        authorities.add(new UserAuthorityEntity(this.id, "user"));
        this.authorities = authorities;
    }

    public User mapToUser() {
        return User.builder().id(id).email(email).nickname(nickname).nightPush(nightPush).push(push).imageUrl(imageUrl)
                .authorities(authorities).refreshKey(refreshKey)
                .secretKey(secretKey).build();
    }

}
