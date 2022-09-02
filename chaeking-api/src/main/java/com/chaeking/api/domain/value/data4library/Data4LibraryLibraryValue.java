package com.chaeking.api.domain.value.data4library;

import com.chaeking.api.domain.entity.Library;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

public class Data4LibraryLibraryValue {
    @Data
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    @Schema(name = "Data4LibraryLibrarySearch")
    public static class Req extends BaseData4LibraryReq {
        private String region;

        @Builder
        public Req(String region, int pageNo, int pageSize) {
            super();
            this.region = region;
            setPageNo(pageNo);
            setPageSize(pageSize);
        }
    }

    @Data
//    @EqualsAndHashCode(callSuper = true)
//    @ToString(callSuper = true)
    @Schema(name = "Data4LibraryLibrary")
    public static class Res { // extends BaseData4LibraryRes {
//        private List<Lib> libs;

        private Response response;

        @Data
        public static class Response {
            // private int pageNo;
            // private int pageSize;
            private List<Lib> libs;
            @JsonProperty("num_found")
            private int numFound;
            private int resultNum;
            private BaseData4LibraryRes.Response.Request request;

            @Data
            public static class Request {
                private int pageNo;
                private int pageSize;
            }

            @Data
            public static class Lib {
                private Library lib;
            }
        }


    }
}
