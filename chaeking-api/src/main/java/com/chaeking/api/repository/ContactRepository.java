package com.chaeking.api.repository;

import com.chaeking.api.domain.entity.Contact;
import com.chaeking.api.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContactRepository extends JpaRepository<Contact, Long> {
    Optional<Contact> findByIdAndUser(Long id, User user);
}
