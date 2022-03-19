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
        private String memo;
    }

    @Data
    public static class BookMemoryWishNewReq extends BookMemoryWishReq {
        private Long bookId;
    }

    @Data
    public static class BookMemoryCompleteReq {
        private String memo;
        private List<Long> tagIds = new ArrayList<>();
        private double rate;
    }

    @Data
    public static class BookMemoryCompleteNewReq extends BookMemoryCompleteReq {
        private Long bookId;
    }
}
