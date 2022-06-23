package com.chaeking.api.service;

import com.chaeking.api.domain.entity.Publisher;
import com.chaeking.api.repository.PublisherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class PublisherService {
    private final PublisherRepository publisherRepository;

    @Transactional
    public Publisher findByName(String name) {
        Publisher publisher =  publisherRepository.findByName(name).orElse(new Publisher(name));
        publisherRepository.save(publisher);
        return publisher;
    }

}
