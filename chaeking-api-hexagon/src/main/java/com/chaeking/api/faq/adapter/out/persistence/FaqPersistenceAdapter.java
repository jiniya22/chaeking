package com.chaeking.api.faq.adapter.out.persistence;

import com.chaeking.api.common.annotation.PersistenceAdapter;
import com.chaeking.api.config.exception.NotFoundException;
import com.chaeking.api.faq.application.port.out.LoadFaqPort;
import com.chaeking.api.faq.domain.Faq;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
@PersistenceAdapter
class FaqPersistenceAdapter implements LoadFaqPort {

    private final FaqRepository faqRepository;
    public static final String MESSAGE_404 = "존재하지 않는 FAQ입니다.";

    @Override
    public List<Faq> loadFaqs(Pageable pageable) {
        return faqRepository.findAll(pageable)
                .stream()
                .map(FaqEntity::mapToFaq)
                .toList();
    }

    @Override
    public Faq loadFaq(long id) {
        return faqRepository.findById(id)
                .map(FaqEntity::mapToFaq)
                .orElseThrow(() -> new NotFoundException(MESSAGE_404));
    }
}
