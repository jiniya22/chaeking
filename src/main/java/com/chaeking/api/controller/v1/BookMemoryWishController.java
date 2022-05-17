package com.chaeking.api.controller.v1;

import com.chaeking.api.domain.value.BookMemoryWishValue;
import com.chaeking.api.domain.value.response.BaseResponse;
import com.chaeking.api.service.BookMemoryWishService;
import com.chaeking.api.util.DescriptionUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Tag(name = "book-memory", description = "북 메모리(이미 읽은 책, 읽고 싶은책)")
@RestController
@RequestMapping("/v1/bookMemories/wish")
public final class BookMemoryWishController {
    private final BookMemoryWishService bookMemoryWishService;

    @Operation(summary = "읽고 싶은 책 등록")
    @PostMapping("")
    public BaseResponse insert(
            @Parameter(description = DescriptionUtils.ID_USER) @RequestHeader(name = "X-User-Id") Long userId,
            @RequestBody BookMemoryWishValue.Req.Creation req) {
        bookMemoryWishService.insert(userId, req);
        return BaseResponse.of();
    }

    @Operation(summary = "읽고 싶은 책 수정")
    @PutMapping("/{bookMemoryWishId}")
    public BaseResponse modify(
            @Parameter(description = DescriptionUtils.ID_USER) @RequestHeader(name = "X-User-Id") Long userId,
            @Parameter(description = DescriptionUtils.ID_BOOK_MEMORY_WISH) @PathVariable(name = "bookMemoryWishId") Long bookMemoryWishId,
            @RequestBody BookMemoryWishValue.Req.Modification req) {
        bookMemoryWishService.modify(userId, bookMemoryWishId, req);
        return BaseResponse.of();
    }

    @Operation(summary = "읽고 싶은 책 삭제")
    @DeleteMapping("/{bookMemoryWishId}")
    public BaseResponse delete(
            @Parameter(description = DescriptionUtils.ID_USER) @RequestHeader(name = "X-User-Id") Long userId,
            @Parameter(description = DescriptionUtils.ID_BOOK_MEMORY_WISH) @PathVariable(name = "bookMemoryWishId") Long bookMemoryWishId) {
        bookMemoryWishService.delete(userId, bookMemoryWishId);
        return BaseResponse.of();
    }
}
