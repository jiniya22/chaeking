package com.chaeking.api.user.domain;

import com.chaeking.api.common.enumerate.Sex;
import com.chaeking.api.common.util.JWTUtils;
import com.chaeking.api.user.application.port.out.UserDetail;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;
import java.util.UUID;

public class User implements UserDetails {
    @Getter
    private Long id;

    private String email;

    @Getter
    private String password;

    private String nickname;

    private Sex sex;

    @Getter
    private String secretKey;

    @Getter
    @Setter
    private String refreshKey;

    private String imageUrl;

    private boolean push;

    private boolean nightPush;


    private boolean active;

    @Getter
    private Set<? extends GrantedAuthority> authorities;

    @Builder
    public User(Long id, String email, String password, String nickname, Sex sex, String secretKey,
                String refreshKey, String imageUrl, boolean push, boolean nightPush, Set<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.sex = sex;
        this.secretKey = secretKey;
        this.refreshKey = refreshKey;
        this.imageUrl = imageUrl;
        this.push = push;
        this.nightPush = nightPush;
        this.authorities = authorities;
    }

    public Token createToken() {
        String key = UUID.randomUUID().toString();
        return new Token(JWTUtils.createAccessToken(id, key), JWTUtils.createRefreshToken(id, key));
    }

    public UserDetail mapToUserDetail() {
        return new UserDetail(email, nickname, imageUrl, push, nightPush);
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isEnabled() {
        return this.active;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.active;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.active;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.active;
    }

}
