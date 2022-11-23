package com.chaeking.api.faq.adapter.out.persistence;

import com.chaeking.api.config.exception.NotFoundException;
import com.chaeking.api.faq.application.port.out.FaqDetail;
import com.chaeking.api.faq.application.port.out.FaqSimple;
import com.chaeking.api.faq.application.port.out.LoadFaqPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
class FaqPersistenceAdapter implements LoadFaqPort {

    private final FaqRepository faqRepository;
    public static final String MESSAGE_404 = "존재하지 않는 FAQ입니다.";

    @Override
    public List<FaqSimple> loadFaqSimples(Pageable pageable) {
        return faqRepository.findAll(pageable)
                .stream()
                .map(FaqEntity::mapToFaqSimple)
                .toList();
    }

    @Override
    public FaqDetail loadFaqDetail(long id) {
        return faqRepository.findById(id)
                .map(FaqEntity::mapToFaqDetail)
                .orElseThrow(() -> new NotFoundException(MESSAGE_404));
    }
}
