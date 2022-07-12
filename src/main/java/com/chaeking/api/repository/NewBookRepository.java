package com.chaeking.api.repository;

import com.chaeking.api.domain.entity.NewBook;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NewBookRepository extends JpaRepository<NewBook, Long> {

    @EntityGraph(attributePaths = {"book", "book.publisher"})
    List<NewBook> findAllWithBookAndPublisherByOrderById();

}
