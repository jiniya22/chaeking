package com.chaeking.api.service;

import com.chaeking.api.config.exception.InvalidInputException;
import com.chaeking.api.domain.dto.data.BookDto;
import com.chaeking.api.domain.entity.Book;
import com.chaeking.api.repository.BookRepository;
import com.chaeking.api.util.DateUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public BookDto.BookRes insert(BookDto.BookReq req) {
        if(StringUtils.isBlank(req.getName()))
            throw new InvalidInputException("name은 필수 입력값입니다.");

        Book book = Book.builder()
                .name(req.getName())
                .isbn(req.getIsbn())
                .price(req.getPrice())
                .author(req.getAuthor())
                .imageUrl(req.getImage_url())
                .publisher(req.getPublisher())
                .detailInfo(req.getDetail_info())
                .publicationDate(DateUtils.stringToDate(req.getPublication_date())).build();
        bookRepository.save(book);

        return new BookDto.BookRes(book);
    }

    public BookDto.BookRes book(long bookId) {
        BookDto.BookRes res = new BookDto.BookRes(select(bookId));
        return res;
    }

}