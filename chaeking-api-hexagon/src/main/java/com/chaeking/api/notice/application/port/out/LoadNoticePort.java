package com.chaeking.api.notice.application.port.out;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface LoadNoticePort {

    List<NoticeSimple> loadNoticeSimples(Pageable pageable);

}
