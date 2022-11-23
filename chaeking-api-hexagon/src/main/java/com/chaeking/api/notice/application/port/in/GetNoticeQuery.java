package com.chaeking.api.notice.application.port.in;

import com.chaeking.api.notice.application.port.out.NoticeSimple;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface GetNoticeQuery {

    List<NoticeSimple> getNotices(Pageable pageable);
}
