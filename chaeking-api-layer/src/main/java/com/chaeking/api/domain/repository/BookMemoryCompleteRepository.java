package com.chaeking.api.domain.repository;

import com.chaeking.api.domain.entity.Book;
import com.chaeking.api.domain.entity.BookMemoryComplete;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookMemoryCompleteRepository extends JpaRepository<BookMemoryComplete, Long> {
    Optional<BookMemoryComplete> findByBookAndUserId(Book book, long userId);

    Optional<BookMemoryComplete> findByIdAndUserId(Long id, long userId);

    @EntityGraph(attributePaths = "book")
    Page<BookMemoryComplete> findAllWithBookByUserId(long userId, Pageable pageable);

    List<BookMemoryComplete> findAllByUserIdAndCreatedAtBetween(long userId, LocalDateTime createdAt1, LocalDateTime createdAt2);

    @EntityGraph(attributePaths = "book")
    Page<BookMemoryComplete> findAllWithByUserIdAndCreatedAtBetween(long userId, LocalDateTime createdAt1, LocalDateTime createdAt2, Pageable pageable);

    boolean existsByUserId(long userId);

    @Modifying
    @Query(value = "delete from BookMemoryComplete b where b.book = ?1 and b.userId = ?2")
    void deleteByBookAndUserId(Book book, long userId);
}
