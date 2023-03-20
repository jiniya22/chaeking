package com.chaeking.api.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.chaeking.api.domain.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
    boolean existsByNickname(String nickname);
    boolean existsByIdNotAndNickname(Long userId, String nickname);
    boolean existsByIdNotAndEmail(Long userId, String email);
}
