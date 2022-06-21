package com.chaeking.api.domain.value;

import io.swagger.v3.oas.annotations.media.Schema;

public class BoardValue {

    public static class Res {
        @Schema(name = "BoardSimple")
        public record Simple(long id, String title, String createdOn) { }

        @Schema(name = "BoardDetail")
        public record Detail(long id, String title, String createdAt, String content) { }
    }
}
