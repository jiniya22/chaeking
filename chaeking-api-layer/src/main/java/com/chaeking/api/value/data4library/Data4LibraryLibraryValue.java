package com.chaeking.api.value.data4library;


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
    public static class Req extends BaseData4LibraryPagingReq {
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
        public static class Response {
            private int pageNo;
            private int pageSize;
            private int numFound;
            private int resultNum;

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


                    public com.chaeking.api.domain.entity.Library convertLibrary(String region) {
                        return com.chaeking.api.domain.entity.Library.builder()
                                .code(this.libCode)
                                .regionCode(region)
                                .name(this.libName)
                                .tel(this.tel)
                                .latitude(this.latitude)
                                .longitude(this.longitude)
                                .address(this.address).build();
                    }
                }
            }
        }

    }
}
