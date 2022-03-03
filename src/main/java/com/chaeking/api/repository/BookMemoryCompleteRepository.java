package com.chaeking.api.repository;

import com.chaeking.api.domain.entity.Book;
import com.chaeking.api.domain.entity.BookMemoryComplete;
import com.chaeking.api.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookMemoryCompleteRepository extends JpaRepository<BookMemoryComplete, Long> {
    Optional<BookMemoryComplete> findByBookAndUser(Book book, User user);
}
