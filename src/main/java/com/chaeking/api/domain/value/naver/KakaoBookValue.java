package com.chaeking.api.domain.value.naver;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

public class KakaoBookValue {

    public static class Res {

        @Data
        public static class BookBasic {
            private List<Document> documents = new ArrayList<>();

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
            }

//            @Data
//            @Schema(name = "KakaoBookMeta")
//            public static class Meta {
////                private boolean is_end;
//                private int pageable_count;
//                private int total_count;
//            }
        }

    }
}
