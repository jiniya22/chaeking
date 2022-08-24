package com.chaeking.api.domain.entity;

import com.chaeking.api.config.SecurityConfig;
import com.chaeking.api.domain.enumerate.Sex;
import com.chaeking.api.domain.value.ChaekingProperties;
import com.chaeking.api.domain.value.TokenValue;
import com.chaeking.api.domain.value.UserValue;
import com.chaeking.api.util.JWTUtils;
import com.chaeking.api.util.cipher.AESCipher;
import lombok.*;
import org.apache.logging.log4j.util.Strings;
import org.hibernate.annotations.*;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Table;
import java.util.*;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@DynamicUpdate
@Table(name = "user")
@Entity
@Where(clause = "active = 1")
public class User extends BaseEntity implements UserDetails {

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
    @Column(columnDefinition = "TINYINT(1)")
    private boolean push;

    @Setter
    @ColumnDefault("false")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    @Column(columnDefinition = "TINYINT(1)")
    private boolean nightPush;

    @Setter
    @Column(length = 40)
    private String refreshKey;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK__USER__USER_AUTHORITY"))
    private Set<UserAuthority> authorities;

    @Setter
    @OneToMany(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    @Where(clause = "user_id IS NOT NULL")
    private List<UserAndAuthor> userAndAuthors = new ArrayList<>();

    @Builder
    private User(String email, String password, String nickname, Sex sex, String secretKey, boolean push, boolean nightPush, String imageUrl) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.sex = sex;
        this.secretKey = secretKey;
        this.push = push;
        this.nightPush = nightPush;
        this.imageUrl = imageUrl;
    }

    public static User of(UserValue.Req.Creation c) {
        String originalPassword = AESCipher.decrypt(c.password(), c.secretKey());
        return User.builder()
                .email(c.email())
                .nickname(c.nickname())
                .sex(Sex.valueOf(c.sex()))
                .secretKey(c.secretKey())
                .password(SecurityConfig.passwordEncoder.encode(originalPassword))
                .push(c.push())
                .nightPush(c.nightPush()).build();
    }

    public static UserValue.Res.Detail createDetail(User u) {
        return new UserValue.Res.Detail(u.getEmail(), u.getNickname(), u.getImageUrl(), u.isPush(), u.isNightPush());
    }

    public static TokenValue.Token createToken(User u) {
        String key = UUID.randomUUID().toString();
        return new TokenValue.Token(JWTUtils.createAccessToken(u, key), JWTUtils.createRefreshToken(u, key));
    }

    public void initializeAuthorities() {
        Set<UserAuthority> authorities = new HashSet<>();
        authorities.add(UserAuthority.builder().userId(this.id).authority("ROLE_USER").build());
        this.authorities = authorities;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isEnabled() {
        return isActive();
    }

    @Override
    public boolean isAccountNonExpired() {
        return isActive();
    }

    @Override
    public boolean isAccountNonLocked() {
        return isActive();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isActive();
    }

    public String getImageUrl() {
        return Strings.isBlank(this.imageUrl) ? ChaekingProperties.getUrl() + "/static/img/user.png" : this.imageUrl;
    }

    public void deactivate() {
        setActive(false);
        this.refreshKey = null;
    }
}
