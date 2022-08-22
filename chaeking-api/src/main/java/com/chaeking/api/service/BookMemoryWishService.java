package com.chaeking.api.service;

import com.chaeking.api.config.exception.InvalidInputException;
import com.chaeking.api.domain.entity.Book;
import com.chaeking.api.domain.entity.BookMemoryComplete;
import com.chaeking.api.domain.entity.BookMemoryWish;
import com.chaeking.api.domain.entity.User;
import com.chaeking.api.domain.value.BookMemoryWishValue;
import com.chaeking.api.domain.value.response.PageResponse;
import com.chaeking.api.repository.BookMemoryCompleteRepository;
import com.chaeking.api.repository.BookMemoryWishRepository;
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
    private final UserService userService;
    private final BookMemoryCompleteRepository bookMemoryCompleteRepository;
    private final BookMemoryWishRepository bookMemoryWishRepository;

    public PageResponse<BookMemoryWishValue.Res.Simple> selectAll(Long userId, String month, Pageable pageable) {
        User user = userService.select(userId);

        Page<BookMemoryWish> bookMemoryWishPage;
        if(Strings.isBlank(month)) {
            bookMemoryWishPage = bookMemoryWishRepository.findAllByUser(user, pageable);
        } else {
            LocalDate date = DateTimeUtils.getFirstDate(month);
            LocalDateTime time1 = DateTimeUtils.getFirstDateTime(date);
            LocalDateTime time2 = DateTimeUtils.getLastDateTime(date);
            bookMemoryWishPage = bookMemoryWishRepository.findAllByUserAndCreatedAtBetween(user, time1, time2, pageable);
        }

        return PageResponse.create(
                bookMemoryWishPage.stream().map(BookMemoryWish::createSimple).collect(Collectors.toList()),
                bookMemoryWishPage.getTotalElements(),
                !bookMemoryWishPage.isLast());
    }

    @Transactional
    public Long insert(Long userId, BookMemoryWishValue.Req.Creation value) {
        User user = userService.select(userId);
        Book book = bookService.select(value.bookId());

        BookMemoryWish bookMemoryWish = bookMemoryWishRepository.findByBookAndUser(book, user)
                .orElse(new BookMemoryWish(book, user));
        bookMemoryWish.setMemo(value.memo());
        bookMemoryCompleteRepository.deleteByBookAndUser(book, user);
        bookMemoryWishRepository.save(bookMemoryWish);

        return bookMemoryWish.getId();
    }

    @Transactional
    public void modify(Long userId, Long bookMemoryWishId, BookMemoryWishValue.Req.Modification value) {
        BookMemoryWish bookMemoryWish = bookMemoryWishRepository.findWithUserById(bookMemoryWishId)
                .orElseThrow(() -> new InvalidInputException(MessageUtils.NOT_FOUND_BOOK_MEMORY_WISH));
        if(!userId.equals(bookMemoryWish.getUser().getId()))
            throw new InvalidInputException(MessageUtils.NOT_FOUND_BOOK_MEMORY_WISH);
        bookMemoryWish.setMemo(value.memo());
        bookMemoryWishRepository.save(bookMemoryWish);
    }

    @Transactional
    public void delete(Long userId, Long bookMemoryWishId) {
        BookMemoryWish bookMemoryWish = bookMemoryWishRepository.findWithUserById(bookMemoryWishId)
                .orElseThrow(() -> new InvalidInputException(MessageUtils.NOT_FOUND_BOOK_MEMORY_WISH));
        if(!userId.equals(bookMemoryWish.getUser().getId()))
            throw new InvalidInputException(MessageUtils.NOT_FOUND_BOOK_MEMORY_WISH);
        bookMemoryWish.setActive(false);
        bookMemoryWishRepository.save(bookMemoryWish);
    }

    public BookMemoryWishValue.Res.Content selectContent(Long userId, Book book) {
        User user = userService.select(userId);
        return bookMemoryWishRepository.findByBookAndUser(book, user).map(BookMemoryWish::createContent).orElse(null);
    }

}
