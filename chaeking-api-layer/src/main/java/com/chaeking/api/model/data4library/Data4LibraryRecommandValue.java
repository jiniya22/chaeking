package com.chaeking.api.model.data4library;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

public class Data4LibraryRecommandValue {

    @Data
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    @Schema(name = "Data4LibraryRecommandSearch")
    public static class Req extends BaseData4LibraryReq {
        private String isbn13;
        private String type = "reader";

        @Builder
        public Req(String isbn13) {
            super();
            this.isbn13 = isbn13;
        }
    }

    @Data
    @Schema(name = "Data4LibraryRecommand")
    public static class Res {
        private Response response;

        @Data
        @JsonNaming(value = PropertyNamingStrategies.LowerCamelCaseStrategy.class)
        public static class Response {
            private int resultNum;

            private List<Doc> docs;

            @Data
            public static class Doc {
                private Book book;
            }

            @Data
            @JsonNaming(value = PropertyNamingStrategies.LowerCamelCaseStrategy.class)
            public static class Book {
                private String bookname;
                private String authors;
                private String publisher;
                private String publication_year;
                private String isbn13;
                private String addition_symbol;
                private String class_nm;
                private String loan_count;
                private String bookImageURL;
            }
        }

    }
}
