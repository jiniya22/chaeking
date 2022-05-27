package com.chaeking.api.domain.value;

import com.chaeking.api.domain.entity.Book;
import com.chaeking.api.domain.entity.BookMemoryComplete;
import com.chaeking.api.domain.entity.BookMemoryWish;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.Optional;

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
        public record Simple(long id, String bookName) {

            public static Simple of(BookMemoryComplete c) {
                return new Simple(c.getId(), Optional.ofNullable(c.getBook()).map(Book::getName).orElse(""));
            }
        }
    }
}
