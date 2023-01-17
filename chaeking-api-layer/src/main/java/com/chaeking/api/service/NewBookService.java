package com.chaeking.api.service;

import com.chaeking.api.domain.entity.Book;
import com.chaeking.api.value.BookValue;
import com.chaeking.api.domain.repository.NewBookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class NewBookService {
    private final NewBookRepository newBookRepository;

    public List<BookValue.Res.Simple> newBooks() {
        return newBookRepository.findAllWithBookAndPublisherByOrderById().stream().map(newBook -> {
            newBook.getBook().getBookAndAuthors().forEach(bookAndAuthor -> bookAndAuthor.getAuthor().getName());
            return Book.createSimple(newBook.getBook());
        }).collect(Collectors.toList());
    }

}
