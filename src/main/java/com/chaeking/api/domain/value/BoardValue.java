package com.chaeking.api.domain.value;

import com.chaeking.api.domain.entity.BaseBoard;
import com.chaeking.api.util.DateTimeUtils;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Optional;

public class BoardValue {

    public static class Res {
        @Schema(name = "BoardSimple")
        public record Simple(long id, String title, String createdOn) {
            public static Simple of(BaseBoard b) {
                return new Simple(b.getId(), b.getTitle(), Optional.ofNullable(b.getCreatedAt()).map(m -> m.format(DateTimeUtils.FORMATTER_DATE)).orElse(null));
            }
        }

        @Schema(name = "BoardDetail")
        public record Detail(long id, String title, String createdAt, String content) {
        }
    }
}
