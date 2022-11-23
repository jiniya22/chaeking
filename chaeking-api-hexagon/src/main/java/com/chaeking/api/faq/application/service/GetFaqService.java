package com.chaeking.api.faq.application.service;

import com.chaeking.api.common.annotation.UseCase;
import com.chaeking.api.faq.application.port.in.GetFaqQuery;
import com.chaeking.api.faq.application.port.out.FaqDetail;
import com.chaeking.api.faq.application.port.out.FaqSimple;
import com.chaeking.api.faq.application.port.out.LoadFaqPort;
import com.chaeking.api.faq.domain.Faq;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@UseCase
class GetFaqService implements GetFaqQuery {
    private final LoadFaqPort loadFaqPort;

    @Override
    public List<FaqSimple> getFaqSimples(Pageable pageable) {
        return loadFaqPort.loadFaqs(pageable).stream().map(Faq::mapToFaqSimple).toList();
    }

    @Override
    public FaqDetail getFaqDetail(long id) {
        return loadFaqPort.loadFaq(id).mapToFaqDetail();
    }
}
