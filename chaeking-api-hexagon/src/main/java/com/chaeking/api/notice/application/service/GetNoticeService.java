package com.chaeking.api.notice.application.service;

import com.chaeking.api.common.annotation.UseCase;
import com.chaeking.api.notice.application.port.in.GetNoticeQuery;
import com.chaeking.api.notice.application.port.out.LoadNoticePort;
import com.chaeking.api.notice.application.port.out.NoticeDetail;
import com.chaeking.api.notice.application.port.out.NoticeSimple;
import com.chaeking.api.notice.domain.Notice;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@UseCase
class GetNoticeService implements GetNoticeQuery {
    private final LoadNoticePort loadNoticePort;

    @Override
    public List<NoticeSimple> getNoticeSimples(Pageable pageable) {
        return loadNoticePort.loadNotices(pageable).stream().map(Notice::mapToNoticeSimple).toList();
    }

    @Override
    public NoticeDetail getNoticeDetail(long id) {
        return loadNoticePort.loadNotice(id).mapToNoticeDetail();
    }
}
