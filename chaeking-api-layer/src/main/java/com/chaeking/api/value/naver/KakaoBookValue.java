package com.chaeking.api.value.naver;

import com.chaeking.api.domain.entity.Book;
import com.chaeking.api.util.DateTimeUtils;
import com.fasterxml.jackson.annotation.JsonSetter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class KakaoBookValue {

    public static class Req {

        @Builder
        @Data
        @Schema(name = "KakaoBookSearch")
        public static class Search {
            private String query;
            private String target;
            private String sort;
            private int page;
            private int size;
        }

    }

    public static class Res {

        @Data
        public static class BookBasic {
            private List<Document> documents = new ArrayList<>();
            private Meta meta;

            @Data
            @Schema(name = "KakaoBookBasic")
            public static class Document {
                private String title;
                private String thumbnail;
                private List<String> authors = new ArrayList<>();
//                private List<String> translators = new ArrayList<>();
                private int price;
                //        private String discount;
                private String publisher;
                private String datetime;
                private String isbn;
                private String url;
                private String contents;

                public Book toBook() {
                    return Book.builder()
                            .name(title)
                            .price(price)
                            .publicationDate(Optional.ofNullable(datetime)
                                    .map(m -> LocalDate.parse(m.replaceAll("\\D", "").substring(0,8), DateTimeUtils.FORMATTER_DATE_SIMPLE)).orElse(null))
                            .isbn(isbn)
                            .imageUrl(thumbnail)
                            .link(url)
                            .detailInfo(contents).build();
                }
            }

            @Data
            @Schema(name = "KakaoBookMeta")
            public static class Meta {
//                @Setter(onMethod_ = {@JsonSetter("is_end")})
                private boolean is_end;
                private int pageable_count;
                private int total_count;
            }
        }

    }
}
