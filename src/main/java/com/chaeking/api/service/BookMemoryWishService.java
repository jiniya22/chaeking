package com.chaeking.api.service;

import com.chaeking.api.config.exception.InvalidInputException;
import com.chaeking.api.domain.entity.Book;
import com.chaeking.api.domain.entity.BookMemoryWish;
import com.chaeking.api.domain.entity.User;
import com.chaeking.api.domain.value.BookMemoryWishValue;
import com.chaeking.api.repository.BookMemoryWishRepository;
import com.chaeking.api.util.MessageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class BookMemoryWishService {
    private final BookService bookService;
    private final UserService userService;
    private final BookMemoryWishRepository bookMemoryWishRepository;

    public List<BookMemoryWishValue.Res.Simple> selectAll(Long userId, Pageable pageable) {
        User user = userService.select(userId);
        return bookMemoryWishRepository.findAllByUser(user, pageable).stream()
                .map(BookMemoryWishValue.Res.Simple::newInstance).collect(Collectors.toList());
    }

    @Transactional
    public void insert(Long userId, BookMemoryWishValue.Req.Creation value) {
        User user = userService.select(userId);
        Book book = bookService.select(value.bookId());

        BookMemoryWish bookMemoryWish = bookMemoryWishRepository.findByBookAndUser(book, user)
                .orElse(new BookMemoryWish(book, user));
        bookMemoryWish.setMemo(value.memo());
        bookMemoryWishRepository.save(bookMemoryWish);
    }

    @Transactional
    public void modify(Long userId, Long bookMemoryWishId, BookMemoryWishValue.Req.Modification value) {
        BookMemoryWish bookMemoryWish = bookMemoryWishRepository.findById(bookMemoryWishId)
                .orElseThrow(() -> new InvalidInputException(MessageUtils.NOT_FOUND_BOOK_MEMORY_WISH));
        if(userId.equals(bookMemoryWish.getUser().getId()))
            throw new InvalidInputException(MessageUtils.NOT_FOUND_BOOK_MEMORY_WISH);
        bookMemoryWish.setMemo(value.memo());
    }

    @Transactional
    public void delete(Long userId, Long bookMemoryWishId) {
        BookMemoryWish bookMemoryWish = bookMemoryWishRepository.findById(bookMemoryWishId)
                .orElseThrow(() -> new InvalidInputException(MessageUtils.NOT_FOUND_BOOK_MEMORY_WISH));
        if(userId.equals(bookMemoryWish.getUser().getId()))
            throw new InvalidInputException(MessageUtils.NOT_FOUND_BOOK_MEMORY_WISH);
        bookMemoryWish.setActive(false);
    }
}
