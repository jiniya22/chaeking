package com.chaeking.api.util;

import java.time.format.DateTimeFormatter;

public final class DateTimeUtils {
    static final String SIMPLE_PATTERN_DATE = "yyyyMMdd";
    static final String DEFAULT_PATTERN_DATE = "yyyy-MM-dd";
    static final String DEFAULT_PATTERN_TIME = "HH:mm:ss";
    static final String DEFAULT_PATTERN_DATETIME = "yyyy-MM-dd HH:mm:ss";

    public static final DateTimeFormatter FORMATTER_DATE_SIMPLE = DateTimeFormatter.ofPattern(SIMPLE_PATTERN_DATE);
    public static final DateTimeFormatter FORMATTER_DATE = DateTimeFormatter.ofPattern(DEFAULT_PATTERN_DATE);
    public static final DateTimeFormatter FORMATTER_TIME = DateTimeFormatter.ofPattern(DEFAULT_PATTERN_TIME);
    public static final DateTimeFormatter FORMATTER_DATETIME = DateTimeFormatter.ofPattern(DEFAULT_PATTERN_DATETIME);
}
