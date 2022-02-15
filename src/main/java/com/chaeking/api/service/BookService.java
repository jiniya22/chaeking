package com.chaeking.api.service;

import com.chaeking.api.config.exception.InvalidInputException;
import com.chaeking.api.domain.dto.data.BookDto;
import com.chaeking.api.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookService {
    private final BookRepository bookRepository;

    public BookDto book(long bookId) {
        BookDto res = bookRepository.findById(bookId).map(BookDto::new)
                .orElseThrow(() -> new InvalidInputException("조회되는 book 정보가 없습니다."));
        return res;
    }
}