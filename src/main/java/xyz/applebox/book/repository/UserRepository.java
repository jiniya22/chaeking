package xyz.applebox.book.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.applebox.book.domain.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
}
