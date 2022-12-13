package com.chaeking.api.notice.adapter.out.persistence;

import com.chaeking.api.config.exception.NotFoundException;
import com.chaeking.api.notice.application.port.out.NoticeDetail;
import com.chaeking.api.notice.domain.Notice;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional(readOnly = true)
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
                PageRequest.of(1, 3, Sort.by(Sort.Order.desc("id"))));
        notices.forEach(System.out::println);
        assertEquals(notices.size(), 3);
        assertEquals(notices.get(0).id(), 11);
    }

    @Test
    @Sql("notice.sql")
    void loadNotice() {
        NoticeDetail noticeDetail = noticePersistenceAdapter.loadNotice(1L).mapToNoticeDetail();
        System.out.println(noticeDetail);
        assertNotNull(noticeDetail);
    }

    @Test
    @Sql("notice.sql")
    void loadNotice_exception() {
        Throwable ex = assertThrows(NotFoundException.class, () -> {
            NoticeDetail noticeDetail = noticePersistenceAdapter.loadNotice(111L).mapToNoticeDetail();
            System.out.println(noticeDetail);
            assertNotNull(noticeDetail);
        });

        assertEquals(ex.getMessage(), NoticePersistenceAdapter.MESSAGE_404);
    }
}