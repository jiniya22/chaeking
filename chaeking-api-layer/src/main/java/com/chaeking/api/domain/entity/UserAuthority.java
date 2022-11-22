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
@Table(name = "user_authority")
@Entity
public class UserAuthority implements GrantedAuthority {

    @Id
    @Column(name = "user_id")
    @JoinColumn(name = "user_id")
    private Long userId;

    @Id
    private String authority;
}
