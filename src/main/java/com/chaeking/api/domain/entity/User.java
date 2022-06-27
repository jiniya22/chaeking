package com.chaeking.api.domain.entity;

import com.chaeking.api.config.SecurityConfig;
import com.chaeking.api.domain.enumerate.Sex;
import com.chaeking.api.domain.value.TokenValue;
import com.chaeking.api.domain.value.UserValue;
import com.chaeking.api.util.JWTUtils;
import com.chaeking.api.util.cipher.AESCipher;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

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

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK__USER__USER_AUTHORITY"))
    private Set<UserAuthority> authorities;

    @Builder
    private User(String email, String password, String nickname, Sex sex, String secretKey) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.sex = sex;
        this.secretKey = secretKey;
    }
    
    public static User of(UserValue.Req.Creation c) {
        String originalPassword = AESCipher.decrypt(c.password(), c.secretKey());
        return User.builder()
                .email(c.email())
                .nickname(c.nickname())
                .sex(Sex.valueOf(c.sex()))
                .secretKey(c.secretKey())
                .password(SecurityConfig.passwordEncoder.encode(originalPassword)).build();
    }

    public static UserValue.Res.Detail createDetail(User u) {
        return new UserValue.Res.Detail(u.getEmail(), u.getNickname(), u.getSex());
    }

    public static TokenValue.Token createToken(User u) {
        return new TokenValue.Token(JWTUtils.createAccessToken(u), JWTUtils.createRefreshToken(u));
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
}
