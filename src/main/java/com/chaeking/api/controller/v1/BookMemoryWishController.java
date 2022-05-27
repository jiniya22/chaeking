package com.chaeking.api.controller.v1;

import com.chaeking.api.domain.value.BookMemoryWishValue;
import com.chaeking.api.domain.value.response.BaseResponse;
import com.chaeking.api.domain.value.response.DataResponse;
import com.chaeking.api.service.BookMemoryWishService;
import com.chaeking.api.util.DescriptionUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Tag(name = "book-memory-wish", description = "북 메모리(읽고 싶은책)")
@RestController
@RequestMapping("/v1/book-memories/wish")
public final class BookMemoryWishController {
    private final BookMemoryWishService bookMemoryWishService;

    @Operation(summary = "읽고 싶은 책 목록")
    @GetMapping("")
    public DataResponse<List<BookMemoryWishValue.Res.Simple>> selectAll(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return DataResponse.of(bookMemoryWishService.selectAll(PageRequest.of(page, size, Sort.by(Sort.Order.desc("id")))));
    }

    @Operation(summary = "읽고 싶은 책 등록")
    @PostMapping("")
    public BaseResponse insert(
            @Parameter(description = DescriptionUtils.ID_USER) @RequestHeader(name = "X-User-Id") Long userId,
            @RequestBody BookMemoryWishValue.Req.Creation req) {
        bookMemoryWishService.insert(userId, req);
        return BaseResponse.of();
    }

    @Operation(summary = "읽고 싶은 책 수정")
    @PutMapping("/{book_memory_wish_id}")
    public BaseResponse modify(
            @Parameter(description = DescriptionUtils.ID_USER) @RequestHeader(name = "X-User-Id") Long userId,
            @Parameter(description = DescriptionUtils.ID_BOOK_MEMORY_WISH) @PathVariable(name = "book_memory_wish_id") Long bookMemoryWishId,
            @RequestBody BookMemoryWishValue.Req.Modification req) {
        bookMemoryWishService.modify(userId, bookMemoryWishId, req);
        return BaseResponse.of();
    }

    @Operation(summary = "읽고 싶은 책 삭제")
    @DeleteMapping("/{book_memory_wish_id}")
    public BaseResponse delete(
            @Parameter(description = DescriptionUtils.ID_USER) @RequestHeader(name = "X-User-Id") Long userId,
            @Parameter(description = DescriptionUtils.ID_BOOK_MEMORY_WISH) @PathVariable(name = "book_memory_wish_id") Long bookMemoryWishId) {
        bookMemoryWishService.delete(userId, bookMemoryWishId);
        return BaseResponse.of();
    }
}
