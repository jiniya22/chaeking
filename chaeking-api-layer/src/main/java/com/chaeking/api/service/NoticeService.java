package com.chaeking.api.service;

import com.chaeking.api.domain.entity.BaseBoard;
import com.chaeking.api.model.BoardValue;
import com.chaeking.api.domain.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class NoticeService {
    private final NoticeRepository noticeRepository;

    public List<BoardValue.Res.Simple> notices(Pageable pageable) {
        return noticeRepository.findAll(pageable)
                .stream()
                .map(BaseBoard::createSimple)
                .collect(Collectors.toList());
    }

    public BoardValue.Res.Detail notice(long id) {
        return noticeRepository.findById(id).map(BaseBoard::createDetail).orElse(null);
    }
}
