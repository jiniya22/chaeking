package com.chaeking.api.faq.adapter.out.persistence;

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

    @Override
    public List<FaqSimple> loadFaqSimples(Pageable pageable) {
        return faqRepository.findAll(pageable)
                .stream()
                .map(FaqEntity::mapToFaqSimple)
                .toList();
    }
}
