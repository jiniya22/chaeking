package com.chaeking.api.service;

import com.chaeking.api.domain.entity.BookMemoryComplete;
import com.chaeking.api.domain.entity.User;
import com.chaeking.api.domain.enumerate.AnalysisType;
import com.chaeking.api.domain.value.BookMemoryCompleteValue;
import com.chaeking.api.domain.value.HomeValue;
import com.chaeking.api.domain.value.response.PageResponse;
import com.chaeking.api.repository.BookMemoryCompleteRepository;
import com.chaeking.api.util.DateTimeUtils;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class BookshelfService {

    private final UserService userService;
    private final BookMemoryCompleteRepository bookMemoryCompleteRepository;

    public PageResponse<BookMemoryCompleteValue.Res.Bookshelf> bookshelf(Long userId, String month, Pageable pageable) {
        if(Strings.isBlank(month))
            month = LocalDate.now().format(DateTimeUtils.FORMATTER_MONTH_SIMPLE);

        LocalDate date = LocalDate.of(Integer.parseInt(month.substring(0, 4)), Integer.parseInt(month.substring(4, 6)), 1);
        LocalDateTime time1 = LocalDateTime.of(date, DateTimeUtils.LOCALTIME_START);
        LocalDateTime time2 = LocalDateTime.of(date.with(TemporalAdjusters.lastDayOfMonth()), DateTimeUtils.LOCALTIME_END);

        User user = userService.select(userId);
        Page<BookMemoryComplete> bookMemoryCompletePage = bookMemoryCompleteRepository.findAllByUserAndCreatedAtBetween(user, time1, time2, pageable);

        return PageResponse.create(
                bookMemoryCompletePage.stream().map(BookMemoryComplete::createBookshelf).collect(Collectors.toList()),
                bookMemoryCompletePage.getTotalElements(),
                !bookMemoryCompletePage.isLast());
    }

    public HomeValue bookAnalysis(Long userId, AnalysisType type) {
        HomeValue res = new HomeValue();
        LocalDate date = LocalDate.now();
        User user = userService.select(userId);
        res.setBookAnalysis(getBookAnalysis(user, date, type));
        return res;
    }

    private HomeValue.BookAnalysis getBookAnalysis(User user, LocalDate date, AnalysisType type) {
        HomeValue.BookAnalysis res = new HomeValue.BookAnalysis(type);
        LocalDateTime time1 = switch(type) {
            case weekly -> LocalDateTime.of(date.minusDays(date.get(ChronoField.DAY_OF_WEEK) - 1).minusWeeks(6), DateTimeUtils.LOCALTIME_START);
            case monthly -> date.minusDays(date.get(ChronoField.DAY_OF_MONTH) - 1).minusMonths(6).atStartOfDay();
            default -> LocalDateTime.of(date.minusDays(date.get(ChronoField.DAY_OF_WEEK) - 1), DateTimeUtils.LOCALTIME_START);
        };
        LocalDateTime time2 = type.equals(AnalysisType.monthly) ?
                LocalDateTime.of(date.minusDays(date.get(ChronoField.DAY_OF_MONTH)).plusMonths(1), DateTimeUtils.LOCALTIME_END) :
                LocalDateTime.of(date.plusDays(7 - date.get(ChronoField.DAY_OF_WEEK)), DateTimeUtils.LOCALTIME_END);

        LocalDateTime[] periodArr = createPeriodArr(type, time1);
        int[] cntArr = {0, 0, 0, 0, 0, 0, 0};

        List<BookMemoryComplete> bookMemoryCompletes = bookMemoryCompleteRepository.findAllByUserAndCreatedAtBetween(user, time1, time2);

        bookMemoryCompletes.forEach(b -> {
            if (b.getCreatedAt().isAfter(periodArr[6])) {
                cntArr[6]++;
            } else if (b.getCreatedAt().isAfter(periodArr[5])) {
                cntArr[5]++;
            } else if (b.getCreatedAt().isAfter(periodArr[4])) {
                cntArr[4]++;
            } else if (b.getCreatedAt().isAfter(periodArr[3])) {
                cntArr[3]++;
            } else if (b.getCreatedAt().isAfter(periodArr[2])) {
                cntArr[2]++;
            } else if (b.getCreatedAt().isAfter(periodArr[1])) {
                cntArr[1]++;
            } else if (b.getCreatedAt().isAfter(periodArr[0])) {
                cntArr[0]++;
            }
        });
        IntStream.range(0, cntArr.length).forEach(i -> {
            String pattern = switch (type) {
                case daily -> "E";
                case monthly -> "MM";
                case weekly -> "MM.dd";
            };
            res.addContent(DateTimeFormatter.ofPattern(pattern).format(periodArr[i]), cntArr[i]);
        });

        return res;
    }

    private LocalDateTime[] createPeriodArr(AnalysisType type, LocalDateTime firstDateTime) {
        LocalDateTime[] periodArr = new LocalDateTime[7];
        for(int i = 0; i < periodArr.length; i++) {
            periodArr[i] = switch (type) {
                case weekly -> firstDateTime.plusWeeks(i);
                case monthly -> firstDateTime.plusMonths(i).with(TemporalAdjusters.firstDayOfMonth());
                default -> firstDateTime.plusDays(i);
            };
        }
        return periodArr;
    }
}