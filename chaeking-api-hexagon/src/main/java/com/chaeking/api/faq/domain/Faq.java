package com.chaeking.api.faq.domain;

import com.chaeking.api.common.util.DateTimeUtils;
import com.chaeking.api.faq.application.port.out.FaqDetail;
import com.chaeking.api.faq.application.port.out.FaqSimple;

import java.time.LocalDateTime;

public record Faq(Long id, String title, String content, LocalDateTime createdAt) {

    public FaqSimple mapToFaqSimple() {
        return new FaqSimple(id, title, DateTimeUtils.toDateString(createdAt));
    }

    public FaqDetail mapToFaqDetail() {
        return new FaqDetail(id, title, content, DateTimeUtils.toString(createdAt));
    }

}
