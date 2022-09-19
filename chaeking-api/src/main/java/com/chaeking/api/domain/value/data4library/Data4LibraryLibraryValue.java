package com.chaeking.api.domain.value.data4library;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
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
    @Schema(name = "Data4LibraryLibrary")
    public static class Res {
        private Response response;

        @Data
        @JsonNaming(value = PropertyNamingStrategies.LowerCamelCaseStrategy.class)
        public static class Response extends BaseData4LibraryResponse {
            private List<Lib> libs;

            @Data
            public static class Lib {
                private Library lib;

                @Data
                @JsonNaming(value = PropertyNamingStrategies.LowerCamelCaseStrategy.class)
                public static class Library {
                    private String libCode;
                    private String libName;
                    private String address;
                    private String tel;
                    private double latitude;
                    private double longitude;
                }
            }
        }

    }
}
