package com.chaeking.api.domain.value.naver;

import com.chaeking.api.domain.entity.Book;
import com.chaeking.api.util.DateTimeUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NaverBookValue {

    public static class Req {

        @Builder
        @Data
        @Schema(name = "KakaoBookSearch")
        public static class Search {
            private String query;
            private String sort;
            private int start;
            private int display;
        }
    }

    public static class Res {

        @Data
        public static class BookBasic {
            //    private String lastBuildDate;
            private long start;
            private long total;
            private int display;
            private List<Item> items = new ArrayList<>();

            @Data
            @Schema(name = "NaverBookBasic")
            public static class Item {
                private String title;
                private String link;
                private String image;
                private String author;
                private int price;
                //        private String discount;
                private String publisher;
                private String pubdate;
                private String isbn;
                private String description;

                public Book toBook() {
                    return Book.builder()
                            .name(title)
                            .price(price)
                            .publicationDate(Optional.ofNullable(pubdate).filter(f -> f != null && f.length() == 8)
                                    .map(m -> LocalDate.parse(m, DateTimeUtils.FORMATTER_DATE_SIMPLE)).orElse(null))
                            .isbn(isbn)
                            .imageUrl(image)
                            .link(link)
                            .detailInfo(description)
                            .build();
                }
            }
        }

    }
}
