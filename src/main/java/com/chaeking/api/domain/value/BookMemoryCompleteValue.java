package com.chaeking.api.domain.value;

import java.util.List;

public final class BookMemoryCompleteValue {

    public static final class Req {
        public record Modification(
                String memo,
                List<Long> tagIds,
                double rate) {
        }

        public record Creation(
                Long bookId,
                String memo,
                List<Long> tagIds,
                double rate) {
        }
    }
}
