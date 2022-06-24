package com.chaeking.api.repository;

import com.chaeking.api.domain.entity.Book;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    boolean existsByIsbn10AndIsbn13(String isbn10, String isbn13);
    boolean existsByIsbn10NullAndIsbn13(String isbn13);

    @EntityGraph(attributePaths = "publisher")
    List<Book> findAllWithPublisherByIdIn(Iterable<Long> ids);

}
