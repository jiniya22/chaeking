package com.chaeking.api.domain.repository;

import com.chaeking.api.domain.entity.Book;
import com.chaeking.api.domain.entity.BookMemoryWish;
import com.chaeking.api.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

public interface BookMemoryWishRepository extends JpaRepository<BookMemoryWish, Long> {
    Optional<BookMemoryWish> findByBookAndUserId(Book book, long userId);
    Optional<BookMemoryWish> findByIdAndUserId(Long id, long userId);
    Page<BookMemoryWish> findAllByUserId(long userId, Pageable pageable);
    Page<BookMemoryWish> findAllByUserIdAndCreatedAtBetween(long userId, LocalDateTime createdAt1, LocalDateTime createdAt2, Pageable pageable);

    @Modifying
    @Query(value = "delete from BookMemoryWish b where b.book = ?1 and b.userId = ?2")
    void deleteByBookAndUserId(Book book, long userId);
}
