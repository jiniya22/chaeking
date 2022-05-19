package com.chaeking.api.domain.value;

import com.chaeking.api.domain.entity.BaseBoard;
import com.chaeking.api.util.DateTimeUtils;

import java.util.Optional;

public class BoardValue {

    public static class Res {
        public record Simple(long id, String title, String createdOn) {
            public static Simple of(BaseBoard b) {
                return new Simple(b.getId(), b.getTitle(), Optional.ofNullable(b.getCreatedAt()).map(m -> m.format(DateTimeUtils.DATE_FORMATTER)).orElse(null));
            }
        }

        public record Detail(long id, String title, String createdAt, String content) {
        }
    }
}
