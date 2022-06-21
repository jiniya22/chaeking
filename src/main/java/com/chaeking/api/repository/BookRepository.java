package com.chaeking.api.repository;

import com.chaeking.api.domain.entity.Book;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    boolean existsByIsbn(String isbn);

    @EntityGraph(attributePaths = "publisher")
    List<Book> findAllWithPublisherByIdIn(Iterable<Long> ids);
}
