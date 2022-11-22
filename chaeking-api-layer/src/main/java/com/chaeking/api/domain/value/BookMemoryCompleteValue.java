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

            public static Modification of(Creation c) {
                return new Modification(c.memo(), c.tagIds(), c.rate());
            }
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
        public record Simple(long id, Long bookId, String bookName, String imageUrl) { }

        @Schema(name = "BookMemoryCompleteBookshelf")
        public record Bookshelf(long id, Long bookId, String bookName, String memo, double rate, String imageUrl) { }

        @Schema(name = "BookMemoryCompleteContent")
        public record Content(
                long id,
                double rate,
                String memo,
                List<Long> tagIds) { }
    }
}
