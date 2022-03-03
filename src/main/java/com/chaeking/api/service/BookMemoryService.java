package com.chaeking.api.service;

import com.chaeking.api.domain.dto.data.BookMemoryDto;
import com.chaeking.api.domain.entity.*;
import com.chaeking.api.repository.BookMemoryCompleteRepository;
import com.chaeking.api.repository.BookMemoryWishRepository;
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
    public void insertBookMemoryComplete(Long chaekingUserId, BookMemoryDto.BookMemoryCompleteReq req) {
        User user = userService.select(chaekingUserId);
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
    public void insertBookMemoryWish(Long chaekingUserId, BookMemoryDto.BookMemoryWishReq req) {
        User user = userService.select(chaekingUserId);
        Book book = bookService.select(req.getBookId());

        BookMemoryWish bookMemoryWish = bookMemoryWishRepository.findByBookAndUser(book, user)
                .orElse(new BookMemoryWish(book, user));
        bookMemoryWish.setMemo(req.getMemo());
        bookMemoryWishRepository.save(bookMemoryWish);
    }


}
