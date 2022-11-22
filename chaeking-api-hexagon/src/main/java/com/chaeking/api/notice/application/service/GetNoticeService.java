package com.chaeking.api.notice.application.service;

import com.chaeking.api.notice.application.port.in.GetNoticeQuery;
import com.chaeking.api.notice.application.port.out.LoadNoticePort;
import com.chaeking.api.notice.domain.Notice;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
class GetNoticeService implements GetNoticeQuery {
    private final LoadNoticePort loadNoticePort;

    @Override
    public List<Notice> getNotices(Pageable pageable) {
        return loadNoticePort.loadNotices(pageable);
    }

//    public BoardValue.Res.Detail notice(long id) {
//        return noticeRepository.findById(id).map(BaseBoard::createDetail).orElse(null);
//    }
}
