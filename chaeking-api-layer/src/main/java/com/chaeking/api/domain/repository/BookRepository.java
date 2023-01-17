package com.chaeking.api.domain.repository;

import com.chaeking.api.domain.entity.Book;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    @EntityGraph(attributePaths = "publisher")
    Optional<Book> findTopWithPublisherByIsbn10AndIsbn13(String isbn10, String isbn13);
    @EntityGraph(attributePaths = "publisher")
    Optional<Book> findTopWithPublisherByIsbn10NullAndIsbn13(String isbn13);
    @EntityGraph(attributePaths = "publisher")
    List<Book> findAllWithPublisherByIdIn(Iterable<Long> ids);
    @EntityGraph(attributePaths = "publisher")
    List<Book> findAllWithPublisherBy(Pageable pageable);
}
