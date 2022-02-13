package xyz.applebox.book.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.applebox.book.domain.entity.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {
}
