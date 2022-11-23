package com.chaeking.api.notice.domain;

import com.chaeking.api.common.util.DateTimeUtils;
import com.chaeking.api.notice.application.port.out.NoticeDetail;
import com.chaeking.api.notice.application.port.out.NoticeSimple;

import java.time.LocalDateTime;

public record Notice(Long id, String title, String content, LocalDateTime createdAt) {
    
    public NoticeSimple mapToNoticeSimple() {
        return new NoticeSimple(id, title, DateTimeUtils.toDateString(createdAt));
    }

    public NoticeDetail mapToNoticeDetail() {
        return new NoticeDetail(id, title, content, DateTimeUtils.toString(createdAt));
    }

}
