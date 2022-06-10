package com.chaeking.api.util;

public final class MessageUtils {
    public static final String SUCCESS = "success";
    public static final String FAIL = "fail";

    public static final String NOT_FOUND_BOOK_MEMORY_COMPLETE = "일치하는 이미 읽은 책 기록이 없습니다";
    public static final String NOT_FOUND_BOOK_MEMORY_WISH = "일치하는 읽고 싶은 책 기록이 없습니다";

    public static final String FORBIDDEN = "접근 권한이 없는 사용자 입니다.";
    public static final String UNAUTHORIZED_AUTHORIZATION_EMPTY = "access_token 이 설정되어 있지 않습니다. (= Authorization Header)";
    public static final String UNAUTHORIZED_AUTHORIZATION_INVALID = "access_token 이 유효하지 않습니다.";
    public static final String UNAUTHORIZED_AUTHORIZATION_FORMAT_ERROR = "access_token 의 형식이 올바르지 않습니다.";
}
