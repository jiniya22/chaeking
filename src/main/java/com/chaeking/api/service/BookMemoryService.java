package com.chaeking.api.service;

import com.chaeking.api.config.exception.InvalidInputException;
import com.chaeking.api.domain.value.BookMemoryDto;
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
    public void insertBookMemoryComplete(Long userId, BookMemoryDto.BookMemoryCompleteNewReq req) {
        User user = userService.select(userId);
        Book book = bookService.select(req.getBookId());
        BookMemoryComplete bookMemoryComplete = bookMemoryCompleteRepository.findByBookAndUser(book, user)
                .orElse(new BookMemoryComplete(book, user));

        bookMemoryComplete.setMemo(req.getMemo());
        bookMemoryComplete.setRate(req.getRate());
        if(!CollectionUtils.isEmpty(req.getTagIds())) {
            bookMemoryComplete.setTags(tagService.select(req.getTagIds())
                    .stream().map(BookMemoryCompleteTag::new).collect(Collectors.toList()));
        }
        bookMemoryCompleteRepository.save(bookMemoryComplete);
    }

    @Transactional
    public void modifyBookMemoryComplete(Long userId, Long bookMemoryCompleteId, BookMemoryDto.BookMemoryCompleteReq req) {
        BookMemoryComplete bookMemoryComplete = bookMemoryCompleteRepository.findById(bookMemoryCompleteId)
                .orElseThrow(() -> new InvalidInputException(MessageUtils.NOT_FOUND_BOOK_MEMORY_COMPLETE));
        if(userId.equals(bookMemoryComplete.getUser().getId()))
            throw new InvalidInputException(MessageUtils.NOT_FOUND_BOOK_MEMORY_COMPLETE);
        bookMemoryComplete.setMemo(req.getMemo());
        bookMemoryComplete.setRate(req.getRate());
        if(!CollectionUtils.isEmpty(req.getTagIds())) {     // TODO :: test 필요
            bookMemoryComplete.setTags(tagService.select(req.getTagIds())
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
    public void insertBookMemoryWish(Long userId, BookMemoryDto.BookMemoryWishNewReq req) {
        User user = userService.select(userId);
        Book book = bookService.select(req.getBookId());

        BookMemoryWish bookMemoryWish = bookMemoryWishRepository.findByBookAndUser(book, user)
                .orElse(new BookMemoryWish(book, user));
        bookMemoryWish.setMemo(req.getMemo());
        bookMemoryWishRepository.save(bookMemoryWish);
    }

    @Transactional
    public void modifyBookMemoryWish(Long userId, Long bookMemoryWishId, BookMemoryDto.BookMemoryWishReq req) {
        BookMemoryWish bookMemoryWish = bookMemoryWishRepository.findById(bookMemoryWishId)
                .orElseThrow(() -> new InvalidInputException(MessageUtils.NOT_FOUND_BOOK_MEMORY_WISH));
        if(userId.equals(bookMemoryWish.getUser().getId()))
            throw new InvalidInputException(MessageUtils.NOT_FOUND_BOOK_MEMORY_WISH);
        bookMemoryWish.setMemo(req.getMemo());
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
