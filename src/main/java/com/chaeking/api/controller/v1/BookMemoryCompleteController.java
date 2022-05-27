package com.chaeking.api.controller.v1;

import com.chaeking.api.domain.value.BookMemoryCompleteValue;
import com.chaeking.api.domain.value.response.BaseResponse;
import com.chaeking.api.domain.value.response.DataResponse;
import com.chaeking.api.service.BookMemoryCompleteService;
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
@Tag(name = "book-memory-complete", description = "북 메모리(이미 읽은 책)")
@RestController
@RequestMapping("/v1/book-memories/complete")
public final class BookMemoryCompleteController {
    private final BookMemoryCompleteService bookMemoryCompleteService;

    @Operation(summary = "이미 읽은 책 목록")
    @GetMapping("")
    public DataResponse<List<BookMemoryCompleteValue.Res.Simple>> selectAll(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return DataResponse.of(bookMemoryCompleteService.selectAll(PageRequest.of(page, size, Sort.by(Sort.Order.desc("id")))));
    }

    @Operation(summary = "이미 읽은 책 등록")
    @PostMapping("")
    public BaseResponse insertBookMemoryComplete(
            @Parameter(description = DescriptionUtils.ID_USER) @RequestHeader(name = "X-User-Id") Long userId,
            @RequestBody BookMemoryCompleteValue.Req.Creation req) {
        bookMemoryCompleteService.insert(userId, req);
        return BaseResponse.of();
    }

    @Operation(summary = "이미 읽은 책 수정")
    @PutMapping("/{book_memory_complete_id}")
    public BaseResponse modifyBookMemoryComplete(
            @Parameter(description = DescriptionUtils.ID_USER) @RequestHeader(name = "X-User-Id") Long userId,
            @Parameter(description = DescriptionUtils.ID_BOOK_MEMORY_COMPLETE) @PathVariable(name = "book_memory_complete_id") Long bookMemoryCompleteId,
            @RequestBody BookMemoryCompleteValue.Req.Modification req) {
        bookMemoryCompleteService.modify(userId, bookMemoryCompleteId, req);
        return BaseResponse.of();
    }

    @Operation(summary = "이미 읽은 책 삭제")
    @DeleteMapping("/{book_memory_complete_id}")
    public BaseResponse deleteBookMemoryComplete(
            @Parameter(description = DescriptionUtils.ID_USER) @RequestHeader(name = "X-User-Id") Long userId,
            @Parameter(description = DescriptionUtils.ID_BOOK_MEMORY_COMPLETE) @PathVariable(name = "book_memory_complete_id") Long bookMemoryCompleteId) {
        bookMemoryCompleteService.delete(userId, bookMemoryCompleteId);
        return BaseResponse.of();
    }

}
