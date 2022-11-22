package com.chaeking.api.notice.domain;

import java.time.LocalDateTime;

public record Notice(Long id, String title, String content, LocalDateTime createdAt) {
}
