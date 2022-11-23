package com.chaeking.api.notice.application.port.in;

import com.chaeking.api.notice.application.port.out.NoticeDetail;
import com.chaeking.api.notice.application.port.out.NoticeSimple;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface GetNoticeQuery {

    List<NoticeSimple> getNoticeSimples(Pageable pageable);

    NoticeDetail getNoticeDetail(long noticeId);
}
