package com.chaeking.api.domain.repository;

import com.chaeking.api.domain.entity.BookAndAuthor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookAndAuthorRepository extends JpaRepository<BookAndAuthor, Long> {
}
