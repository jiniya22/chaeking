package com.chaeking.api.domain.value;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public final class BookMemoryCompleteValue {

    public static final class Req {
        @Schema(name = "BookMemoryCompleteModification")
        public record Modification(
                String memo,
                List<Long> tagIds,
                double rate) {
        }

        @Schema(name = "BookMemoryCompleteCreation")
        public record Creation(
                Long bookId,
                String memo,
                List<Long> tagIds,
                double rate) {
        }
    }

    public static final class Res {
        @Schema(name = "BookMemoryCompleteSimple")
        public record Simple(long id, String bookName) { }
    }
}
