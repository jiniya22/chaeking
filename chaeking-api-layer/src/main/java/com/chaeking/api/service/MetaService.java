package com.chaeking.api.service;

import com.chaeking.api.domain.entity.Meta;
import com.chaeking.api.model.enumerate.MetaType;
import com.chaeking.api.domain.repository.MetaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MetaService {
    private final MetaRepository metaRepository;

    public String meta(MetaType type) {
        return metaRepository.findByType(type).map(Meta::getContent).orElse(null);
    }

}
