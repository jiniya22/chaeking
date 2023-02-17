package com.chaeking.api.domain.repository;

import com.chaeking.api.domain.entity.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContactRepository extends JpaRepository<Contact, Long> {
    Optional<Contact> findByIdAndUserId(Long id, long userId);

    Page<Contact> findByUserId(long userId, Pageable pageable);
}
