package com.chaeking.api.faq.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

interface FaqRepository extends JpaRepository<FaqEntity, Long> {
}
