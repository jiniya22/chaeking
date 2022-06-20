package com.chaeking.api.repository;

import com.chaeking.api.domain.entity.Book;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    @EntityGraph(attributePaths = "publisher")
    Optional<Book> findWithPublisherByIsbn(String isbn);
}
