package com.chaeking.api.service;

import com.chaeking.api.domain.entity.BaseBoard;
import com.chaeking.api.domain.value.BoardValue;
import com.chaeking.api.repository.FaqRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FaqService {
    private final FaqRepository faqRepository;

    public List<BoardValue.Res.Simple> faqs(Pageable pageable) {
        return faqRepository.findAll(pageable)
                .stream()
                .map(BaseBoard::createSimple)
                .collect(Collectors.toList());
    }

    public BoardValue.Res.Detail faq(long id) {
        return faqRepository.findById(id).map(BaseBoard::createDetail).orElse(null);
    }
}
