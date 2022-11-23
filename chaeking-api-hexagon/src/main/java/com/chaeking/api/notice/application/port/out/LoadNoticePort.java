package com.chaeking.api.notice.application.port.out;

import com.chaeking.api.notice.domain.Notice;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface LoadNoticePort {

    List<Notice> loadNotices(Pageable pageable);

    Notice loadNotice(long id);

}
