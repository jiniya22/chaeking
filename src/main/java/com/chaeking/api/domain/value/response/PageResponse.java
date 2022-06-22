package com.chaeking.api.domain.value.response;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class PageResponse<T> extends DataResponse {
    private List<T> data;
    private long totalCount;
    private boolean hasNext;

    protected PageResponse(Page<T> data) {
        this.data = data.getContent();
        this.totalCount = data.getTotalElements();
        this.hasNext = !data.isLast();
    }

    private PageResponse(List<T> data, long totalCount, boolean hasNext) {
        this.data = data;
        this.totalCount = totalCount;
        this.hasNext = hasNext;
    }

    public static <T> PageResponse<T> create(Page<T> data) {
        return new PageResponse<>(data);
    }

    public static <T> PageResponse<T> create(List<T> data, long totalCount, boolean hasNext) {
        return new PageResponse(data, totalCount, hasNext);
    }
}
