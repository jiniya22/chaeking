package com.chaeking.api.model;

import io.swagger.v3.oas.annotations.media.Schema;

public class ContactValue {

    public static class Res {
        @Schema(name = "BoardSimple")
        public record Simple(long id, String title, String createdOn) { }

        @Schema(name = "ContactDetail")
        public record Detail(long id, String title, String createdAt, String content, String status, String answerTitle, String answerContent) { }
    }
}
