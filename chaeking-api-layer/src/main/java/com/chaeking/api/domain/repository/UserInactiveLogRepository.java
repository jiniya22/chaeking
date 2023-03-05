package com.chaeking.api.domain.repository;

import com.chaeking.api.domain.entity.UserInactiveLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInactiveLogRepository extends JpaRepository<UserInactiveLog, Long> {
}
