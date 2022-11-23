package com.chaeking.api.notice.application.service;

import com.chaeking.api.common.annotation.UseCase;
import com.chaeking.api.notice.application.port.in.GetNoticeQuery;
import com.chaeking.api.notice.application.port.out.LoadNoticePort;
import com.chaeking.api.notice.application.port.out.NoticeDetail;
import com.chaeking.api.notice.application.port.out.NoticeSimple;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@UseCase
class GetNoticeService implements GetNoticeQuery {
    private final LoadNoticePort loadNoticePort;

    @Override
    public List<NoticeSimple> getNotices(Pageable pageable) {
        return loadNoticePort.loadNoticeSimples(pageable);
    }

    @Override
    public NoticeDetail getNotice(long id) {
        return loadNoticePort.loadNoticeDetail(id);
    }
}
