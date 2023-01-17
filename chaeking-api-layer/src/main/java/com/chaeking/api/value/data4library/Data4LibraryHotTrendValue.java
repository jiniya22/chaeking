package com.chaeking.api.value.data4library;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

public class Data4LibraryHotTrendValue {

    @Data
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    @Schema(name = "Data4LibraryHotTrendSearch")
    public static class Req extends BaseData4LibraryReq {
        private String searchDt;

        @Builder
        public Req(String searchDt) {
            super();
            this.searchDt = searchDt;
        }
    }

    @Data
    @Schema(name = "Data4LibraryHotTrend")
    public static class Res {
        private Response response;

        @Data
        @JsonNaming(value = PropertyNamingStrategies.LowerCamelCaseStrategy.class)
        public static class Response {
            private int resultNum;
            private List<Results> results;

            @Data
            public static class Results {
                private Result result;

                @Data
                public static class Result {
                    private String date;

                    private List<Docs> docs;

                    @Data
                    public static class Docs {
                        private Doc doc;

                        @Data
                        @JsonNaming(value = PropertyNamingStrategies.LowerCamelCaseStrategy.class)
                        public static class Doc {
                            private String bookname;
                            private String authors;
                            private String publisher;
                            private String publication_year;
                            private String isbn13;
                            private String class_nm;
                            @JsonProperty("bookImageURL")
                            private String bookImageURL;
                        }
                    }
                }
            }

        }

    }
}
