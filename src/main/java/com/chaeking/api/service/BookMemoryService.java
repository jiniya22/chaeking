package com.chaeking.api.service;

import com.chaeking.api.config.exception.InvalidInputException;
import com.chaeking.api.domain.value.BookMemoryCompleteValue;
import com.chaeking.api.domain.value.BookMemoryWishValue;
import com.chaeking.api.domain.entity.*;
import com.chaeking.api.repository.BookMemoryCompleteRepository;
import com.chaeking.api.repository.BookMemoryWishRepository;
import com.chaeking.api.util.MessageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class BookMemoryService {
    private final BookService bookService;
    private final UserService userService;
    private final TagService tagService;
    private final BookMemoryWishRepository bookMemoryWishRepository;
    private final BookMemoryCompleteRepository bookMemoryCompleteRepository;

    @Transactional
    public void insertBookMemoryComplete(Long userId, BookMemoryCompleteValue.Req.Creation req) {
        User user = userService.select(userId);
        Book book = bookService.select(req.bookId());
        BookMemoryComplete bookMemoryComplete = bookMemoryCompleteRepository.findByBookAndUser(book, user)
                .orElse(new BookMemoryComplete(book, user));

        bookMemoryComplete.setMemo(req.memo());
        bookMemoryComplete.setRate(req.rate());
        if(!CollectionUtils.isEmpty(req.tagIds())) {
            bookMemoryComplete.setTags(tagService.select(req.tagIds())
                    .stream().map(BookMemoryCompleteTag::new).collect(Collectors.toList()));
        }
        bookMemoryCompleteRepository.save(bookMemoryComplete);
    }

    @Transactional
    public void modifyBookMemoryComplete(Long userId, Long bookMemoryCompleteId, BookMemoryCompleteValue.Req.Modification value) {
        BookMemoryComplete bookMemoryComplete = bookMemoryCompleteRepository.findById(bookMemoryCompleteId)
                .orElseThrow(() -> new InvalidInputException(MessageUtils.NOT_FOUND_BOOK_MEMORY_COMPLETE));
        if(userId.equals(bookMemoryComplete.getUser().getId()))
            throw new InvalidInputException(MessageUtils.NOT_FOUND_BOOK_MEMORY_COMPLETE);
        bookMemoryComplete.setMemo(value.memo());
        bookMemoryComplete.setRate(value.rate());
        if(!CollectionUtils.isEmpty(value.tagIds())) {     // TODO :: test 필요
            bookMemoryComplete.setTags(tagService.select(value.tagIds())
                    .stream().map(BookMemoryCompleteTag::new).collect(Collectors.toList()));
        }
    }

    @Transactional
    public void deleteBookMemoryComplete(Long userId, Long bookMemoryCompleteId) {
        BookMemoryComplete bookMemoryComplete = bookMemoryCompleteRepository.findById(bookMemoryCompleteId)
                .orElseThrow(() -> new InvalidInputException(MessageUtils.NOT_FOUND_BOOK_MEMORY_COMPLETE));
        if(userId.equals(bookMemoryComplete.getUser().getId()))
            throw new InvalidInputException(MessageUtils.NOT_FOUND_BOOK_MEMORY_COMPLETE);
        bookMemoryComplete.setActive(false);
    }

    @Transactional
    public void insertBookMemoryWish(Long userId, BookMemoryWishValue.Req.Creation value) {
        User user = userService.select(userId);
        Book book = bookService.select(value.bookId());

        BookMemoryWish bookMemoryWish = bookMemoryWishRepository.findByBookAndUser(book, user)
                .orElse(new BookMemoryWish(book, user));
        bookMemoryWish.setMemo(value.memo());
        bookMemoryWishRepository.save(bookMemoryWish);
    }

    @Transactional
    public void modifyBookMemoryWish(Long userId, Long bookMemoryWishId, BookMemoryWishValue.Req.Modification value) {
        BookMemoryWish bookMemoryWish = bookMemoryWishRepository.findById(bookMemoryWishId)
                .orElseThrow(() -> new InvalidInputException(MessageUtils.NOT_FOUND_BOOK_MEMORY_WISH));
        if(userId.equals(bookMemoryWish.getUser().getId()))
            throw new InvalidInputException(MessageUtils.NOT_FOUND_BOOK_MEMORY_WISH);
        bookMemoryWish.setMemo(value.memo());
    }

    @Transactional
    public void deleteBookMemoryWish(Long userId, Long bookMemoryWishId) {
        BookMemoryWish bookMemoryWish = bookMemoryWishRepository.findById(bookMemoryWishId)
                .orElseThrow(() -> new InvalidInputException(MessageUtils.NOT_FOUND_BOOK_MEMORY_WISH));
        if(userId.equals(bookMemoryWish.getUser().getId()))
            throw new InvalidInputException(MessageUtils.NOT_FOUND_BOOK_MEMORY_WISH);
        bookMemoryWish.setActive(false);
    }
}
