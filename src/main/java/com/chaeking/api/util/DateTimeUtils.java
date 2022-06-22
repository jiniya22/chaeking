package com.chaeking.api.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public final class DateTimeUtils {
    static final String SIMPLE_PATTERN_MONTH = "yyyyMM";
    static final String SIMPLE_PATTERN_DATE = "yyyyMMdd";
    static final String DEFAULT_PATTERN_DATE = "yyyy-MM-dd";
    static final String DEFAULT_PATTERN_TIME = "HH:mm:ss";
    static final String DEFAULT_PATTERN_DATETIME = "yyyy-MM-dd HH:mm:ss";

    public static final DateTimeFormatter FORMATTER_MONTH_SIMPLE = DateTimeFormatter.ofPattern(SIMPLE_PATTERN_MONTH);
    public static final DateTimeFormatter FORMATTER_DATE_SIMPLE = DateTimeFormatter.ofPattern(SIMPLE_PATTERN_DATE);
    public static final DateTimeFormatter FORMATTER_DATE = DateTimeFormatter.ofPattern(DEFAULT_PATTERN_DATE);
    public static final DateTimeFormatter FORMATTER_TIME = DateTimeFormatter.ofPattern(DEFAULT_PATTERN_TIME);
    public static final DateTimeFormatter FORMATTER_DATETIME = DateTimeFormatter.ofPattern(DEFAULT_PATTERN_DATETIME);

    public static final LocalTime LOCALTIME_START = LocalTime.of(0, 0);
    public static final LocalTime LOCALTIME_END = LocalTime.of(23, 59, 59);

    public static String toString(LocalDate localDate) {
        return Optional.ofNullable(localDate).map(m -> m.format(DateTimeUtils.FORMATTER_DATE)).orElse(null);
    }

    public static String toString(LocalDateTime localDateTime) {
        return Optional.ofNullable(localDateTime).map(m -> m.format(DateTimeUtils.FORMATTER_DATE)).orElse(null);
    }
}
