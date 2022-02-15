package com.chaeking.api.service;

import com.chaeking.api.domain.dto.data.BaseDto;
import com.chaeking.api.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TagService {
    private final TagRepository tagRepository;

    public List<BaseDto> tags() {
        return tagRepository.findAll()
                .stream()
                .map(m -> new BaseDto(m.getId(), m.getName()))
                .collect(Collectors.toList());
    }
}
