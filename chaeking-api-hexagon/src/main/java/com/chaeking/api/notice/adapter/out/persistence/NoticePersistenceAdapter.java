package com.chaeking.api.notice.adapter.out.persistence;

import com.chaeking.api.notice.application.port.out.LoadNoticePort;
import com.chaeking.api.notice.domain.Notice;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class NoticePersistenceAdapter implements LoadNoticePort {

    private final NoticeRepository noticeRepository;

    @Override
    public List<Notice> loadNotices(Pageable pageable) {
        return noticeRepository.findAll(pageable)
                .stream()
                .map(NoticeEntity::mapToNotice)
                .toList();
    }
}
