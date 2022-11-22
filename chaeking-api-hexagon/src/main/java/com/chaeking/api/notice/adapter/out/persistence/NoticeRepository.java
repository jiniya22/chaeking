package com.chaeking.api.notice.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

interface NoticeRepository extends JpaRepository<NoticeEntity, Long> {
}
