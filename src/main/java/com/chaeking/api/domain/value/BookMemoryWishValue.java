package com.chaeking.api.domain.value;

import com.chaeking.api.domain.entity.Book;
import com.chaeking.api.domain.entity.BookMemoryWish;
import com.chaeking.api.domain.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Setter;

import javax.persistence.*;
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

            public static Simple of(BookMemoryWish w) {
                return new Simple(w.getId(), Optional.ofNullable(w.getBook()).map(Book::getName).orElse(""));
            }
        }
    }
}
