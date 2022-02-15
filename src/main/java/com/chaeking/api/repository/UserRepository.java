package com.chaeking.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.chaeking.api.domain.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
}
