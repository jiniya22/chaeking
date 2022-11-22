package com.chaeking.api.repository;

import com.chaeking.api.domain.entity.Faq;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FaqRepository extends JpaRepository<Faq, Long> {
}
