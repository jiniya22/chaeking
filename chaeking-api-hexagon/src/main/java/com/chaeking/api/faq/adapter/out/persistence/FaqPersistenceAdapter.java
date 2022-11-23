package com.chaeking.api.faq.adapter.out.persistence;

import com.chaeking.api.faq.application.port.out.LoadFaqPort;
import com.chaeking.api.faq.domain.Faq;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class FaqPersistenceAdapter implements LoadFaqPort {

    private final FaqRepository faqRepository;

    @Override
    public List<Faq> loadFaqs(Pageable pageable) {
        return faqRepository.findAll(pageable)
                .stream()
                .map(FaqEntity::mapToFaq)
                .toList();
    }
}
