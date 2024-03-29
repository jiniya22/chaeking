package com.chaeking.api.service;

import com.chaeking.api.config.exception.InvalidInputException;
import com.chaeking.api.domain.entity.Book;
import com.chaeking.api.domain.entity.BookMemoryWish;
import com.chaeking.api.domain.repository.BookMemoryCompleteRepository;
import com.chaeking.api.domain.repository.BookMemoryWishRepository;
import com.chaeking.api.model.BookMemoryWishValue;
import com.chaeking.api.model.response.PageResponse;
import com.chaeking.api.util.DateTimeUtils;
import com.chaeking.api.util.MessageUtils;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class BookMemoryWishService {
    private final BookService bookService;
    private final BookMemoryCompleteRepository bookMemoryCompleteRepository;
    private final BookMemoryWishRepository bookMemoryWishRepository;

    public PageResponse<BookMemoryWishValue.Res.Simple> selectAll(Long userId, String month, Pageable pageable) {
        Page<BookMemoryWish> bookMemoryWishPage;
        if(Strings.isBlank(month)) {
            bookMemoryWishPage = bookMemoryWishRepository.findAllByUserId(userId, pageable);
        } else {
            LocalDate date = DateTimeUtils.getFirstDate(month);
            LocalDateTime time1 = DateTimeUtils.getFirstDateTime(date);
            LocalDateTime time2 = DateTimeUtils.getLastDateTime(date);
            bookMemoryWishPage = bookMemoryWishRepository.findAllByUserIdAndCreatedAtBetween(userId, time1, time2, pageable);
        }

        return PageResponse.create(
                bookMemoryWishPage.stream().map(BookMemoryWish::createSimple).collect(Collectors.toList()),
                bookMemoryWishPage.getTotalElements(),
                !bookMemoryWishPage.isLast());
    }

    @Transactional
    public Long insert(Long userId, BookMemoryWishValue.Req.Creation value) {
        var book = bookService.select(value.bookId());

        var bookMemoryWish = bookMemoryWishRepository.findByBookAndUserId(book, userId)
                .orElse(new BookMemoryWish(book, userId));
        bookMemoryWish.setMemo(value.memo());
        bookMemoryCompleteRepository.deleteByBookAndUserId(book, userId);
        bookMemoryWishRepository.save(bookMemoryWish);

        return bookMemoryWish.getId();
    }

    @Transactional
    public void modify(Long userId, Long bookMemoryWishId, BookMemoryWishValue.Req.Modification value) {
        var bookMemoryWish = bookMemoryWishRepository.findByIdAndUserId(bookMemoryWishId, userId)
                .orElseThrow(() -> new InvalidInputException(MessageUtils.NOT_FOUND_BOOK_MEMORY_WISH));
        bookMemoryWish.setMemo(value.memo());
        bookMemoryWishRepository.save(bookMemoryWish);
    }

    @Transactional
    public void delete(Long userId, Long bookMemoryWishId) {
        var bookMemoryWish = bookMemoryWishRepository.findByIdAndUserId(bookMemoryWishId, userId)
                .orElseThrow(() -> new InvalidInputException(MessageUtils.NOT_FOUND_BOOK_MEMORY_WISH));
        bookMemoryWishRepository.delete(bookMemoryWish);
    }

    public BookMemoryWishValue.Res.Content selectContent(Long userId, Book book) {
        return bookMemoryWishRepository.findByBookAndUserId(book, userId).map(BookMemoryWish::createContent).orElse(null);
    }

}
