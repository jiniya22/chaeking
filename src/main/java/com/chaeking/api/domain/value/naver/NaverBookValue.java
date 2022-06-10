package com.chaeking.api.domain.value.naver;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

public class NaverBookValue {

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
                private String images;
                private String author;
                private String price;
                //        private String discount;
                private String publisher;
                private String isbn;
                private String description;
            }
        }

    }
}
