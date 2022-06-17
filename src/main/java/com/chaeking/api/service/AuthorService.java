package com.chaeking.api.service;

import com.chaeking.api.domain.entity.Author;
import com.chaeking.api.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class AuthorService {
    private final AuthorRepository authorRepository;

    @Transactional
    public Author findByName(String name) {
        Author author =  authorRepository.findByName(name).orElse(new Author(name));
        authorRepository.save(author);
        return author;
    }

    @Transactional
    public List<Author> findAllByNameIn(List<String> names) {
        List<Author> authors = names.stream().map(name -> findByName(name)).collect(Collectors.toList());
        return authors;
    }
}
