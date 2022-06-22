package com.chaeking.api.domain.value.response;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class PageResponse<T> extends DataResponse {
    private List<T> data;
    private long totalCount;
    private boolean isEnd;

    protected PageResponse(Page<T> data) {
        this.data = data.getContent();
        this.totalCount = data.getTotalElements();
        this.isEnd = data.isLast();
    }

    private PageResponse(List<T> data, long totalCount, boolean isEnd) {
        this.data = data;
        this.totalCount = totalCount;
        this.isEnd = isEnd;
    }

    public static <T> PageResponse<T> create(Page<T> data) {
        return new PageResponse<>(data);
    }

    public static <T> PageResponse<T> create(List<T> data, long totalCount, boolean isEnd) {
        return new PageResponse(data, totalCount, isEnd);
    }
}
