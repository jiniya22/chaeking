package com.chaeking.api.domain.value;

import com.chaeking.api.domain.entity.Book;
import com.chaeking.api.domain.entity.BookMemoryWish;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Optional;

public final class BookMemoryWishValue {

    public static final class Req {
        @Schema(name = "BookMemoryWishModification")
        public record Modification(String memo) {
        }

        @Schema(name = "BookMemoryWishCreation")
        public record Creation(String memo, Long bookId) {
        }

    }

    public static final class Res {
        @Schema(name = "BookMemoryWishSimple")
        public record Simple(long id, String bookName) {

            public static Simple create(BookMemoryWish w) {
                return new Simple(w.getId(), Optional.ofNullable(w.getBook()).map(Book::getName).orElse(""));
            }
        }
    }
}
