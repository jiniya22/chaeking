package com.chaeking.api.controller.v1;

import com.chaeking.api.domain.value.BookMemoryCompleteValue;
import com.chaeking.api.domain.value.response.BaseResponse;
import com.chaeking.api.service.BookMemoryComplateService;
import com.chaeking.api.util.DescriptionUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Tag(name = "book-memory-complete", description = "북 메모리(이미 읽은 책)")
@RestController
@RequestMapping("/v1/bookMemories/complete")
public final class BookMemoryComplateController {
    private final BookMemoryComplateService bookMemoryComplateService;

    @Operation(summary = "이미 읽은 책 등록")
    @PostMapping("")
    public BaseResponse insertBookMemoryComplete(
            @Parameter(description = DescriptionUtils.ID_USER) @RequestHeader(name = "X-User-Id") Long userId,
            @RequestBody BookMemoryCompleteValue.Req.Creation req) {
        bookMemoryComplateService.insert(userId, req);
        return BaseResponse.of();
    }

    @Operation(summary = "이미 읽은 책 수정")
    @PutMapping("/{bookMemoryCompleteId}")
    public BaseResponse modifyBookMemoryComplete(
            @Parameter(description = DescriptionUtils.ID_USER) @RequestHeader(name = "X-User-Id") Long userId,
            @Parameter(description = DescriptionUtils.ID_BOOK_MEMORY_COMPLETE) @PathVariable(name = "bookMemoryCompleteId") Long bookMemoryCompleteId,
            @RequestBody BookMemoryCompleteValue.Req.Modification req) {
        bookMemoryComplateService.modify(userId, bookMemoryCompleteId, req);
        return BaseResponse.of();
    }

    @Operation(summary = "이미 읽은 책 삭제")
    @DeleteMapping("/{bookMemoryCompleteId}")
    public BaseResponse deleteBookMemoryComplete(
            @Parameter(description = DescriptionUtils.ID_USER) @RequestHeader(name = "X-User-Id") Long userId,
            @Parameter(description = DescriptionUtils.ID_BOOK_MEMORY_COMPLETE) @PathVariable(name = "bookMemoryCompleteId") Long bookMemoryCompleteId) {
        bookMemoryComplateService.delete(userId, bookMemoryCompleteId);
        return BaseResponse.of();
    }

}
