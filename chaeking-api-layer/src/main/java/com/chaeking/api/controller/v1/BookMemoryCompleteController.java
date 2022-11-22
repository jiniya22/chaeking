package com.chaeking.api.controller.v1;

import com.chaeking.api.domain.value.BookMemoryCompleteValue;
import com.chaeking.api.domain.value.response.BaseResponse;
import com.chaeking.api.domain.value.response.PageResponse;
import com.chaeking.api.service.BookMemoryCompleteService;
import com.chaeking.api.util.BasicUtils;
import com.chaeking.api.util.DescriptionUtils;
import com.chaeking.api.util.RegexpUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.constraints.Pattern;

@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
@Tag(name = "book-memory-complete", description = "북 메모리(이미 읽은 책)")
@RestController
@RequestMapping("/v1/book-memories/complete")
public class BookMemoryCompleteController {
    private final BookMemoryCompleteService bookMemoryCompleteService;

    @Operation(summary = "이미 읽은 책 목록")
    @GetMapping("")
    public PageResponse<BookMemoryCompleteValue.Res.Simple> selectAll(
            @Parameter(description = DescriptionUtils.MONTH) @RequestParam(required = false) @Pattern(regexp = RegexpUtils.MONTH) String month,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        Long userId = BasicUtils.getUserId();
        return bookMemoryCompleteService.selectAll(userId, month, PageRequest.of(page, size, Sort.by(Sort.Order.desc("id"))));
    }

    @Operation(summary = "이미 읽은 책 등록", responses = @ApiResponse(responseCode = "201"))
    @PostMapping("")
    public ResponseEntity<BaseResponse> insertBookMemoryComplete(
            @RequestBody BookMemoryCompleteValue.Req.Creation req) {
        Long userId = BasicUtils.getUserId();
        Long bookMemoryCompleteId = bookMemoryCompleteService.insert(userId, req);

        return ResponseEntity
                .created(ServletUriComponentsBuilder.fromCurrentRequest()
                        .path("/{book_memory_complete_id}").buildAndExpand(bookMemoryCompleteId).toUri())
                .body(BaseResponse.SUCCESS_INSTANCE);
    }

    @Operation(summary = "이미 읽은 책 수정")
    @PutMapping("/{book_memory_complete_id}")
    public BaseResponse modifyBookMemoryComplete(
            @Parameter(description = DescriptionUtils.ID_BOOK_MEMORY_COMPLETE) @PathVariable(name = "book_memory_complete_id") Long bookMemoryCompleteId,
            @RequestBody BookMemoryCompleteValue.Req.Modification req) {
        Long userId = BasicUtils.getUserId();
        bookMemoryCompleteService.modify(userId, bookMemoryCompleteId, req);
        return BaseResponse.SUCCESS_INSTANCE;
    }

    @Operation(summary = "이미 읽은 책 삭제")
    @DeleteMapping("/{book_memory_complete_id}")
    public BaseResponse deleteBookMemoryComplete(
            @Parameter(description = DescriptionUtils.ID_BOOK_MEMORY_COMPLETE) @PathVariable(name = "book_memory_complete_id") Long bookMemoryCompleteId) {
        Long userId = BasicUtils.getUserId();
        bookMemoryCompleteService.delete(userId, bookMemoryCompleteId);
        return BaseResponse.SUCCESS_INSTANCE;
    }

}
