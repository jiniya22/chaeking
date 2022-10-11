package com.chaeking.api.domain.value.data4library;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

public class Data4LibraryLoanItemValue {
    @JsonNaming(value = PropertyNamingStrategies.LowerCamelCaseStrategy.class)
    @Data
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    @Schema(name = "Data4LibraryLoanItemSearch")
    public static class Req extends BaseData4LibraryPagingReq {
        private String startDt;
        private String endDt;

        @Builder
        public Req(String startDt, String endDt, int pageNo, int pageSize) {
            super();
            this.startDt = startDt;
            this.endDt = endDt;
            setPageNo(pageNo);
            setPageSize(pageSize);
        }
    }

    @Data
    @Schema(name = "Data4LibraryLoanItem")
    public static class Res {
        private Response response;

        @Data
        @JsonNaming(value = PropertyNamingStrategies.LowerCamelCaseStrategy.class)
        public static class Response {
            private int resultNum;

            private List<Doc> docs;

            @Data
            public static class Doc {
                private Document doc;

                @Data
                @JsonNaming(value = PropertyNamingStrategies.LowerCamelCaseStrategy.class)
                public static class Document {
                    private int ranking;
                    private String bookname;
                    private String authors;
                    private String publisher;
                    private String publicagtion_year;
                    private String isbn13;
                    private String addition_symbol;
                    private String class_nm;
                    private String loan_count;
                    private String bookImageURL;
                }
            }
        }

    }
}
