package com.chaeking.api.notice.adapter.out.persistence;

import com.chaeking.api.notice.application.port.out.NoticeDetail;
import com.chaeking.api.notice.domain.Notice;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(NoticePersistenceAdapter.class)
class NoticePersistenceAdapterTest {
    @Autowired
    NoticePersistenceAdapter noticePersistenceAdapter;

    @Autowired
    NoticeRepository noticeRepository;


    @Test
    @Sql("notice.sql")
    void loadNotices() {
        List<Notice> notices = noticePersistenceAdapter.loadNotices(
                PageRequest.of(0, 10, Sort.by(Sort.Order.desc("id"))));
        notices.forEach(System.out::println);
        assertEquals(notices.size(), 2);
    }

    @Test
    @Sql("notice.sql")
    void loadNotice() {
        NoticeDetail noticeDetail = noticePersistenceAdapter.loadNotice(1L).mapToNoticeDetail();
        System.out.println(noticeDetail);
        assertNotNull(noticeDetail);
    }
}