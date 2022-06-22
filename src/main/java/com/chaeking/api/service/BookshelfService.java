package com.chaeking.api.service;

import com.chaeking.api.domain.entity.BookMemoryComplete;
import com.chaeking.api.domain.entity.User;
import com.chaeking.api.domain.value.BookMemoryCompleteValue;
import com.chaeking.api.domain.value.response.PageResponse;
import com.chaeking.api.repository.BookMemoryCompleteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class BookshelfService {

    private final UserService userService;
    private final BookMemoryCompleteRepository bookMemoryCompleteRepository;

    public PageResponse<BookMemoryCompleteValue.Res.Bookshelf> select(Long userId, String month, Pageable pageable) {
        LocalDate date = LocalDate.of(Integer.valueOf(month.substring(0, 4)), Integer.valueOf(month.substring(4, 6)), 1);
        LocalDateTime time1 = LocalDateTime.of(date, LocalTime.of(0, 0));
        LocalDateTime time2 = LocalDateTime.of(date.with(TemporalAdjusters.lastDayOfMonth()), LocalTime.of(23, 59, 59));

        User user = userService.select(userId);
        Page<BookMemoryComplete> bookMemoryCompletePage = bookMemoryCompleteRepository.findAllByUserAndCreatedAtBetween(user, time1, time2, pageable);

        return PageResponse.create(
                bookMemoryCompletePage.stream().map(BookMemoryComplete::createBookshelf).collect(Collectors.toList()),
                bookMemoryCompletePage.getTotalElements(),
                bookMemoryCompletePage.isLast());
    }
}
