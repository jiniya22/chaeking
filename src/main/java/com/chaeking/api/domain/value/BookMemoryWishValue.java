package com.chaeking.api.domain.value;

public final class BookMemoryWishValue {

    public static final class Req {
        public record Modification(String memo) {
        }

        public record Creation(String memo, Long bookId) {
        }

    }

}
