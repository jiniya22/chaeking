package com.chaeking.api.service;

import com.chaeking.api.domain.value.BaseValue;
import com.chaeking.api.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class TagService {
    private final TagRepository tagRepository;

    public List<BaseValue> tags() {
        return tagRepository.findAll().stream().map(m -> new BaseValue(m.getId(), m.getName())).collect(Collectors.toList());
    }
}
