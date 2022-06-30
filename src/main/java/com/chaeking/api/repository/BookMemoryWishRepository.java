package com.chaeking.api.repository;

import com.chaeking.api.domain.entity.Book;
import com.chaeking.api.domain.entity.BookMemoryWish;
import com.chaeking.api.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookMemoryWishRepository extends JpaRepository<BookMemoryWish, Long> {
    Optional<BookMemoryWish> findByBookAndUser(Book book, User user);
    Optional<BookMemoryWish> findWithUserById(Long bookMemoryWishId);
    Page<BookMemoryWish> findAllByUser(User user, Pageable pageable);
}
