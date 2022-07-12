package com.chaeking.api.domain.value.response;

import com.chaeking.api.domain.entity.BookMemoryWish;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

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

//    public static <T, R> PageResponse<R> create(Page<T> data, Function f, Class<R> clazz) {
////        bookMemoryWishPage.stream().map(BookMemoryWish::createSimple).collect(Collectors.toList())
//        data.stream().map(T::createSimple).collect(Collectors.toList());
//        return new PageResponse<>(data);
//    }

    public static <T> PageResponse<T> create(List<T> data, long totalCount, boolean hasNext) {
        return new PageResponse(data, totalCount, hasNext);
    }
}
