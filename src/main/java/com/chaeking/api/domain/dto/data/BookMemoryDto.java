package com.chaeking.api.domain.dto.data;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class BookMemoryDto {

    @Data
    public static class BookMemoryWishReq {
        private Long bookId;
        private String memo;
    }

    @Data
    public static class BookMemoryWishRes extends BookMemoryWishReq {
        private long id;
    }

    @Data
    public static class BookMemoryCompleteReq extends BookMemoryWishReq {
        private List<Long> tagIds = new ArrayList<>();
        private double rate;
    }
}
