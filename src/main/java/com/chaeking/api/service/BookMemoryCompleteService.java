package com.chaeking.api.service;

import com.chaeking.api.config.exception.InvalidInputException;
import com.chaeking.api.domain.entity.Book;
import com.chaeking.api.domain.entity.BookMemoryComplete;
import com.chaeking.api.domain.entity.BookMemoryCompleteTag;
import com.chaeking.api.domain.entity.User;
import com.chaeking.api.domain.value.BookMemoryCompleteValue;
import com.chaeking.api.repository.BookMemoryCompleteRepository;
import com.chaeking.api.util.MessageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class BookMemoryCompleteService {
    private final BookService bookService;
    private final UserService userService;
    private final TagService tagService;
    private final BookMemoryCompleteRepository bookMemoryCompleteRepository;

    public List<BookMemoryCompleteValue.Res.Simple> selectAll(Long userId, Pageable pageable) {
        User user = userService.select(userId);
        return bookMemoryCompleteRepository.findAllByUser(user, pageable)
                .stream()
                .map(BookMemoryComplete::createSimple)
                .collect(Collectors.toList());
    }

    @Transactional
    public void insert(Long userId, BookMemoryCompleteValue.Req.Creation req) {
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
    public void modify(Long userId, Long bookMemoryCompleteId, BookMemoryCompleteValue.Req.Modification value) {
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
    public void delete(Long userId, Long bookMemoryCompleteId) {
        BookMemoryComplete bookMemoryComplete = bookMemoryCompleteRepository.findById(bookMemoryCompleteId)
                .orElseThrow(() -> new InvalidInputException(MessageUtils.NOT_FOUND_BOOK_MEMORY_COMPLETE));
        if(userId.equals(bookMemoryComplete.getUser().getId()))
            throw new InvalidInputException(MessageUtils.NOT_FOUND_BOOK_MEMORY_COMPLETE);
        bookMemoryComplete.setActive(false);
    }

}
