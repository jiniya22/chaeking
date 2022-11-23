package com.chaeking.api.faq.domain;

import java.time.LocalDateTime;

public record Faq(Long id, String title, String content, LocalDateTime createdAt) {
}
