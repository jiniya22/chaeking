package com.chaeking.api.service;

import com.chaeking.api.domain.entity.BestSeller;
import com.chaeking.api.domain.entity.Book;
import com.chaeking.api.domain.entity.BookMemoryComplete;
import com.chaeking.api.domain.entity.User;
import com.chaeking.api.model.enumerate.AnalysisType;
import com.chaeking.api.model.BookMemoryCompleteValue;
import com.chaeking.api.model.HomeValue;
import com.chaeking.api.model.response.PageResponse;
import com.chaeking.api.domain.repository.BestSellerRepository;
import com.chaeking.api.domain.repository.BookMemoryCompleteRepository;
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
    private final BestSellerRepository bestSellerRepository;
    private final BookMemoryCompleteRepository bookMemoryCompleteRepository;

    public PageResponse<BookMemoryCompleteValue.Res.Bookshelf> bookshelf(Long userId, String month, Pageable pageable) {
        if(Strings.isBlank(month))
            month = LocalDate.now().format(DateTimeUtils.FORMATTER_MONTH_SIMPLE);

        LocalDate date = DateTimeUtils.getFirstDate(month);
        LocalDateTime time1 = DateTimeUtils.getFirstDateTime(date);
        LocalDateTime time2 = DateTimeUtils.getLastDateTime(date);

        Page<BookMemoryComplete> bookMemoryCompletePage = bookMemoryCompleteRepository.findAllWithByUserIdAndCreatedAtBetween(userId, time1, time2, pageable);

        return PageResponse.create(
                bookMemoryCompletePage.stream().map(BookMemoryComplete::createBookshelf).collect(Collectors.toList()),
                bookMemoryCompletePage.getTotalElements(),
                !bookMemoryCompletePage.isLast());
    }

    public HomeValue bookAnalysis(Long userId, AnalysisType type) {
        HomeValue res = new HomeValue();
        LocalDate date = LocalDate.now();
        User user = userService.select(userId);
        res.setNickname(user.getNickname());
        res.setBookAnalysis(getBookAnalysis(user.getId(), date, type));
        List<BestSeller> bestSellers = bestSellerRepository.findTop3WithBookAndPublisherByOrderById();
        res.setBestSeller(bestSellers.stream()
                .map(BestSeller::getBook)
                .map(Book::createSimple).collect(Collectors.toList()));
        return res;
    }

    private HomeValue.BookAnalysis getBookAnalysis(long userId, LocalDate date, AnalysisType type) {
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

        List<BookMemoryComplete> bookMemoryCompletes = bookMemoryCompleteRepository.findAllByUserIdAndCreatedAtBetween(userId, time1, time2);

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