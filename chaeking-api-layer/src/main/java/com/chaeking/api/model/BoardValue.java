package com.chaeking.api.model;

import io.swagger.v3.oas.annotations.media.Schema;

public class BoardValue {

    public static final class Req {
        @Schema(name = "BoardCreation")
        public record Creation(String title, String content) { }
    }

    public static class Res {
        @Schema(name = "BoardSimple")
        public record Simple(long id, String title, String createdOn) { }

        @Schema(name = "BoardDetail")
        public record Detail(long id, String title, String createdAt, String content) { }
    }
}
