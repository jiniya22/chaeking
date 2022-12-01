package com.chaeking.api.faq.adapter.out.persistence;

import com.chaeking.api.faq.application.port.out.FaqDetail;
import com.chaeking.api.faq.domain.Faq;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Transactional(readOnly = true)
@DataJpaTest
@Import(FaqPersistenceAdapter.class)
class FaqPersistenceAdapterTest {

    @Autowired
    FaqPersistenceAdapter faqPersistenceAdapter;

    @Autowired
    FaqRepository faqRepository;


    @Test
    @Sql("faq.sql")
    void loadFaqs() {
        List<Faq> faqs = faqPersistenceAdapter.loadFaqs(
                PageRequest.of(1, 3, Sort.by(Sort.Order.desc("id"))));
        faqs.forEach(System.out::println);
        assertEquals(faqs.size(), 3);
        assertEquals(faqs.get(0).id(), 11);
    }

    @Test
    @Sql("faq.sql")
    void loadFaq() {
        FaqDetail faqDetail = faqPersistenceAdapter.loadFaq(1L).mapToFaqDetail();
        System.out.println(faqDetail);
        assertNotNull(faqDetail);
    }
}