package com.chaeking.api.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

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
        public record Simple(long id, Long bookId, String bookName, String imageUrl, String authors, String publisher) { }

        @Schema(name = "BookMemoryWishContent")
        public record Content(
                long id,
                String memo) { }
    }
}
