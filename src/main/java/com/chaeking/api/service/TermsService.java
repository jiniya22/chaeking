package com.chaeking.api.service;

import com.chaeking.api.domain.entity.Terms;
import com.chaeking.api.domain.value.TermsValue;
import com.chaeking.api.repository.TermsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class TermsService {
    private final TermsRepository termsRepository;

    public List<TermsValue> terms() {
        return termsRepository.findAll()
                .stream()
                .map(Terms::createDetail)
                .collect(Collectors.toList());
    }

}
