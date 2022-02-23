package com.chaeking.api.service;

import com.chaeking.api.domain.dto.data.BoardDto;
import com.chaeking.api.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class NoticeService {
    private final NoticeRepository noticeRepository;

    public List<BoardDto> notices(Pageable pageable) {
        return noticeRepository.findAll(pageable)
                .stream()
                .map(m -> new BoardDto(m.getId(), m.getTitle(), m.getContent()))
                .collect(Collectors.toList());
    }
}
