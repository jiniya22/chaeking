package com.chaeking.api.service;

import com.chaeking.api.config.exception.InvalidInputException;
import com.chaeking.api.domain.entity.BookMemoryComplete;
import com.chaeking.api.domain.entity.BookMemoryCompleteTag;
import com.chaeking.api.domain.entity.User;
import com.chaeking.api.value.BookMemoryCompleteValue;
import com.chaeking.api.value.response.PageResponse;
import com.chaeking.api.domain.repository.BookMemoryCompleteRepository;
import com.chaeking.api.domain.repository.BookMemoryWishRepository;
import com.chaeking.api.domain.repository.TagRepository;
import com.chaeking.api.util.DateTimeUtils;
import com.chaeking.api.util.MessageUtils;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class BookMemoryCompleteService {
    private final BookService bookService;
    private final UserService userService;
    private final TagRepository tagRepository;
    private final BookMemoryCompleteRepository bookMemoryCompleteRepository;
    private final BookMemoryWishRepository bookMemoryWishRepository;

    public PageResponse<BookMemoryCompleteValue.Res.Simple> selectAll(Long userId, String month, Pageable pageable) {
        User user = userService.select(userId);

        Page<BookMemoryComplete> bookMemoryCompletePage;
        if(Strings.isBlank(month)) {
            bookMemoryCompletePage = bookMemoryCompleteRepository.findAllByUser(user, pageable);
        } else {
            LocalDate date = DateTimeUtils.getFirstDate(month);
            LocalDateTime time1 = DateTimeUtils.getFirstDateTime(date);
            LocalDateTime time2 = DateTimeUtils.getLastDateTime(date);
            bookMemoryCompletePage = bookMemoryCompleteRepository.findAllByUserAndCreatedAtBetween(user, time1, time2, pageable);
        }

        return PageResponse.create(
                bookMemoryCompletePage.stream().map(BookMemoryComplete::createSimple).collect(Collectors.toList()),
                bookMemoryCompletePage.getTotalElements(),
                !bookMemoryCompletePage.isLast());
    }

    @Transactional
    public Long insert(Long userId, BookMemoryCompleteValue.Req.Creation req) {
        var user = userService.select(userId);
        var book = bookService.select(req.bookId());
        var bookMemoryComplete = bookMemoryCompleteRepository.findByBookAndUser(book, user)
                .orElse(new BookMemoryComplete(book, user));

        mergeBookMemoryComplete(bookMemoryComplete, BookMemoryCompleteValue.Req.Modification.of(req));
        return bookMemoryComplete.getId();
    }

    @Transactional
    public void modify(Long userId, Long bookMemoryCompleteId, BookMemoryCompleteValue.Req.Modification req) {
        var bookMemoryComplete = bookMemoryCompleteRepository.findWithUserById(bookMemoryCompleteId)
                .orElseThrow(() -> new InvalidInputException(MessageUtils.NOT_FOUND_BOOK_MEMORY_COMPLETE));
        if(!userId.equals(bookMemoryComplete.getUser().getId()))
            throw new InvalidInputException(MessageUtils.NOT_FOUND_BOOK_MEMORY_COMPLETE);

        mergeBookMemoryComplete(bookMemoryComplete, req);
    }

    @Transactional
    public void delete(Long userId, Long bookMemoryCompleteId) {
        var bookMemoryComplete = bookMemoryCompleteRepository.findWithUserById(bookMemoryCompleteId)
                .orElseThrow(() -> new InvalidInputException(MessageUtils.NOT_FOUND_BOOK_MEMORY_COMPLETE));
        if(!userId.equals(bookMemoryComplete.getUser().getId()))
            throw new InvalidInputException(MessageUtils.NOT_FOUND_BOOK_MEMORY_COMPLETE);
        bookMemoryComplete.setActive(false);
        bookMemoryCompleteRepository.save(bookMemoryComplete);
    }

    private void mergeBookMemoryComplete(BookMemoryComplete bookMemoryComplete, BookMemoryCompleteValue.Req.Modification req) {
        bookMemoryComplete.setMemo(req.memo());
        bookMemoryComplete.setRate(req.rate());
        mergeBookMemoryCompleteTags(bookMemoryComplete, req.tagIds());
        bookMemoryWishRepository.deleteByBookAndUser(bookMemoryComplete.getBook(), bookMemoryComplete.getUser());
        bookMemoryCompleteRepository.save(bookMemoryComplete);
    }

    private void mergeBookMemoryCompleteTags(BookMemoryComplete bookMemoryComplete, List<Long> tagIds) {
        var bookMemoryCompleteTags = bookMemoryComplete.getTags();
        if(!CollectionUtils.isEmpty(tagIds)) {
            bookMemoryComplete.removeTags(bookMemoryCompleteTags.stream().filter(f -> !tagIds.contains(f.getTag().getId())).collect(Collectors.toList()));
            var existIds = bookMemoryCompleteTags.stream().mapToLong(m -> m.getTag().getId()).boxed().toList();
            tagIds.stream().filter(tagId -> !existIds.contains(tagId)).forEach(tagId -> tagRepository.findById(tagId).ifPresent(tag -> bookMemoryComplete.addTag(new BookMemoryCompleteTag(tag))));
        } else if (!CollectionUtils.isEmpty(bookMemoryCompleteTags)) {
            bookMemoryComplete.removeTags(bookMemoryCompleteTags);
        }
    }

}
