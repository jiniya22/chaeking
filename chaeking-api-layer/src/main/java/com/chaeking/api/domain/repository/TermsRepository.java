package com.chaeking.api.domain.repository;

import com.chaeking.api.domain.entity.Terms;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TermsRepository extends JpaRepository<Terms, Long> {
}
