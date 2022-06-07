package com.chaeking.api.domain.entity;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@IdClass(UserAuthority.class)
@Entity
public class UserAuthority implements GrantedAuthority {

    @Id
    @JoinColumn(name = "user_id")
    private Long userId;

    @Id
    private String authority;
}
