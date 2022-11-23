package com.chaeking.api.notice.adapter.out.persistence;

import com.chaeking.api.config.exception.NotFoundException;
import com.chaeking.api.notice.application.port.out.LoadNoticePort;
import com.chaeking.api.notice.application.port.out.NoticeDetail;
import com.chaeking.api.notice.application.port.out.NoticeSimple;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
class NoticePersistenceAdapter implements LoadNoticePort {

    private final NoticeRepository noticeRepository;
    public static final String MESSAGE_404 = "존재하지 않는 공지사항입니다.";

    @Override
    public List<NoticeSimple> loadNoticeSimples(Pageable pageable) {
        return noticeRepository.findAll(pageable)
                .stream()
                .map(NoticeEntity::mapToNoticeSimple)
                .toList();
    }

    @Override
    public NoticeDetail loadNoticeDetail(long id) {
        return noticeRepository.findById(id)
                .map(NoticeEntity::mapToNoticeDetail)
                .orElseThrow(() -> new NotFoundException(MESSAGE_404));
    }
}
