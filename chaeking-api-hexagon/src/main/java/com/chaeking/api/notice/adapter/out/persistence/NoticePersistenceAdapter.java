package com.chaeking.api.notice.adapter.out.persistence;

import com.chaeking.api.common.annotation.PersistenceAdapter;
import com.chaeking.api.config.exception.NotFoundException;
import com.chaeking.api.notice.application.port.out.LoadNoticePort;
import com.chaeking.api.notice.domain.Notice;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
@PersistenceAdapter
class NoticePersistenceAdapter implements LoadNoticePort {

    private final NoticeRepository noticeRepository;
    public static final String MESSAGE_404 = "존재하지 않는 공지사항입니다.";

    @Override
    public List<Notice> loadNotices(Pageable pageable) {
        return noticeRepository.findAll(pageable)
                .stream()
                .map(NoticeEntity::mapToNotice)
                .toList();
    }

    @Override
    public Notice loadNotice(long id) {
        return noticeRepository.findById(id)
                .map(NoticeEntity::mapToNotice)
                .orElseThrow(() -> new NotFoundException(MESSAGE_404));
    }
}
