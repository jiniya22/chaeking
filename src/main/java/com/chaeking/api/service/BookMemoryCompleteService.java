package com.chaeking.api.service;

import com.chaeking.api.config.exception.InvalidInputException;
import com.chaeking.api.domain.entity.Book;
import com.chaeking.api.domain.entity.BookMemoryComplete;
import com.chaeking.api.domain.entity.BookMemoryCompleteTag;
import com.chaeking.api.domain.entity.User;
import com.chaeking.api.domain.value.BookMemoryCompleteValue;
import com.chaeking.api.repository.BookMemoryCompleteRepository;
import com.chaeking.api.repository.BookMemoryWishRepository;
import com.chaeking.api.repository.TagRepository;
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
    private final TagRepository tagRepository;
    private final BookMemoryCompleteRepository bookMemoryCompleteRepository;
    private final BookMemoryWishRepository bookMemoryWishRepository;

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

        mergeBookMemoryComplete(bookMemoryComplete, BookMemoryCompleteValue.Req.Modification.of(req));
    }

    @Transactional
    public void modify(Long userId, Long bookMemoryCompleteId, BookMemoryCompleteValue.Req.Modification req) {
        BookMemoryComplete bookMemoryComplete = bookMemoryCompleteRepository.findWithUserById(bookMemoryCompleteId)
                .orElseThrow(() -> new InvalidInputException(MessageUtils.NOT_FOUND_BOOK_MEMORY_COMPLETE));
        if(!userId.equals(bookMemoryComplete.getUser().getId()))
            throw new InvalidInputException(MessageUtils.NOT_FOUND_BOOK_MEMORY_COMPLETE);

        mergeBookMemoryComplete(bookMemoryComplete, req);
    }

    @Transactional
    public void delete(Long userId, Long bookMemoryCompleteId) {
        BookMemoryComplete bookMemoryComplete = bookMemoryCompleteRepository.findWithUserById(bookMemoryCompleteId)
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
        List<BookMemoryCompleteTag> bookMemoryCompleteTags = bookMemoryComplete.getTags();
        if(!CollectionUtils.isEmpty(tagIds)) {
            bookMemoryComplete.removeTags(bookMemoryCompleteTags.stream().filter(f -> !tagIds.contains(f.getTag().getId())).collect(Collectors.toList()));
            List<Long> existIds = bookMemoryCompleteTags.stream().mapToLong(m -> m.getTag().getId()).boxed().collect(Collectors.toList());
            tagIds.stream().filter(tagId -> !existIds.contains(tagId)).forEach(tagId -> {
                tagRepository.findById(tagId).ifPresent(tag -> {
                    bookMemoryComplete.addTag(new BookMemoryCompleteTag(tag));
                });
            });
        } else if (!CollectionUtils.isEmpty(bookMemoryCompleteTags)) {
            bookMemoryComplete.removeTags(bookMemoryCompleteTags);
        }
    }

}
