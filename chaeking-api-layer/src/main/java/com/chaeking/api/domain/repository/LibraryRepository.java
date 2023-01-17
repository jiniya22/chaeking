package com.chaeking.api.domain.repository;

import com.chaeking.api.domain.entity.Library;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LibraryRepository extends JpaRepository<Library, Long> {

    Optional<Library> findByCode(String code);

}
