package com.chaeking.api.domain.value.data4library;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public abstract class BaseData4LibraryRes {
    private Response response;

    @Data
    public static class Response {
        // private int pageNo;
        // private int pageSize;
        @JsonProperty("num_found")
        private int numFound;
        private int resultNum;
        private Request request;

        @Data
        public static class Request {
            private int pageNo;
            private int pageSize;
        }
    }
}
