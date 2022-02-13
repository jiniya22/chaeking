package xyz.applebox.book.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xyz.applebox.book.domain.dto.data.BaseDto;
import xyz.applebox.book.repository.TagRepository;

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
