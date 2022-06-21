package com.chaeking.api.domain.value;

import com.chaeking.api.domain.entity.BaseBoard;
import com.chaeking.api.util.DateTimeUtils;
import io.swagger.v3.oas.annotations.media.Schema;

public class BoardValue {

    public static class Res {
        @Schema(name = "BoardSimple")
        public record Simple(long id, String title, String createdOn) {
            public static Simple newInstance(BaseBoard b) {
                return new Simple(b.getId(), b.getTitle(), DateTimeUtils.toString(b.getCreatedAt()));
            }
        }

        @Schema(name = "BoardDetail")
        public record Detail(long id, String title, String createdAt, String content) {

            public static Detail newInstance(BaseBoard b) {
                return new Detail(b.getId(), b.getTitle(), DateTimeUtils.toString(b.getCreatedAt()), b.getContent());
            }
        }
    }
}
