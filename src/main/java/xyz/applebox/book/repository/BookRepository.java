package xyz.applebox.book.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.applebox.book.domain.entity.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
}
