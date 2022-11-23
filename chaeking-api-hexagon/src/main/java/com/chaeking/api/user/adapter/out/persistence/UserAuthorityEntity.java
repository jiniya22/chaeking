package com.chaeking.api.user.adapter.out.persistence;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@IdClass(UserAuthorityEntity.class)
@Table(name = "user_authority")
@Entity
class UserAuthorityEntity implements GrantedAuthority {
    @Id
    @Column(name = "user_id")
    @JoinColumn(name = "user_id")
    private Long userId;

    @Id
    private String authority;

}
