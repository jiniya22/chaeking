package com.chaeking.api.service;

import com.chaeking.api.config.exception.InvalidInputException;
import com.chaeking.api.domain.value.BookValue;
import com.chaeking.api.domain.entity.Book;
import com.chaeking.api.repository.BookRepository;
import com.chaeking.api.util.DateTimeUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class BookService {
    private final BookRepository bookRepository;

    public Book select(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new InvalidInputException("조회되는 book 정보가 없습니다(book_id Error)"));
        return book;
    }

    @Transactional
    public BookValue.Res.Detail insert(BookValue.Req.Creation req) {
        if(StringUtils.isBlank(req.name()))
            throw new InvalidInputException("name은 필수 입력값입니다.");

        Book book = Book.builder()
                .name(req.name())
                .isbn(req.isbn())
                .price(req.price())
                .author(req.author())
                .imageUrl(req.image_url())
                .publisher(req.publisher())
                .detailInfo(req.detail_info())
                .publicationDate(Optional.ofNullable(req.publication_date())
                        .map(m -> LocalDate.parse(m, DateTimeUtils.DATE_FORMATTER)).orElse(null)).build();
        bookRepository.save(book);

        return new BookValue.Res.Detail(book);
    }

    public BookValue.Res.Detail book(long bookId) {
        BookValue.Res.Detail res = new BookValue.Res.Detail(select(bookId));
        return res;
    }

}