package com.chaeking.api.notice.adapter.out.persistence;

import com.chaeking.api.notice.application.port.out.LoadNoticePort;
import com.chaeking.api.notice.application.port.out.NoticeSimple;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
class NoticePersistenceAdapter implements LoadNoticePort {

    private final NoticeRepository noticeRepository;

    @Override
    public List<NoticeSimple> loadNoticeSimples(Pageable pageable) {
        return noticeRepository.findAll(pageable)
                .stream()
                .map(NoticeEntity::mapToNoticeSimple)
                .toList();
    }
}
