package com.chaeking.api.domain.value;

import io.swagger.v3.oas.annotations.media.Schema;

public final class BookMemoryWishValue {

    public static final class Req {
        @Schema(name = "UserModification")
        public record Modification(String memo) {
        }

        @Schema(name = "UserCreation")
        public record Creation(String memo, Long bookId) {
        }

    }

}
