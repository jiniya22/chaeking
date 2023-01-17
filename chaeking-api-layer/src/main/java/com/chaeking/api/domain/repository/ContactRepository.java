package com.chaeking.api.domain.repository;

import com.chaeking.api.domain.entity.Contact;
import com.chaeking.api.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ContactRepository extends JpaRepository<Contact, Long> {
    Optional<Contact> findByIdAndUser(Long id, User user);

    Page<Contact> findByUser(User user, Pageable pageable);
}
