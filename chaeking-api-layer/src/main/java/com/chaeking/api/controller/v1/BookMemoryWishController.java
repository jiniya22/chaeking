package com.chaeking.api.controller.v1;

import com.chaeking.api.value.BookMemoryWishValue;
import com.chaeking.api.value.response.BaseResponse;
import com.chaeking.api.value.response.PageResponse;
import com.chaeking.api.service.BookMemoryWishService;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.constraints.Pattern;

@RequiredArgsConstructor
@Tag(name = "book-memory-wish", description = "북 메모리(읽고 싶은책)")
@RestController
@RequestMapping("/v1/book-memories/wish")
public class BookMemoryWishController {
    private final BookMemoryWishService bookMemoryWishService;

    @Operation(summary = "읽고 싶은 책 목록")
    @GetMapping("")
    public PageResponse<BookMemoryWishValue.Res.Simple> selectAll(
            @Parameter(description = DescriptionUtils.MONTH) @RequestParam(required = false) @Pattern(regexp = RegexpUtils.MONTH) String month,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        Long userId = BasicUtils.getUserId();
        return bookMemoryWishService.selectAll(userId, month, PageRequest.of(page, size, Sort.by(Sort.Order.desc("id"))));
    }

    @Operation(summary = "읽고 싶은 책 등록", responses = @ApiResponse(responseCode = "201"))
    @PostMapping("")
    public ResponseEntity<BaseResponse> insert(
            @RequestBody BookMemoryWishValue.Req.Creation req) {
        Long userId = BasicUtils.getUserId();
        Long bookMemoryWishId = bookMemoryWishService.insert(userId, req);
        return ResponseEntity
                .created(ServletUriComponentsBuilder.fromCurrentRequest()
                        .path("/{book_memory_wish_id}").buildAndExpand(bookMemoryWishId).toUri())
                .body(BaseResponse.SUCCESS_INSTANCE);
    }

    @Operation(summary = "읽고 싶은 책 수정")
    @PutMapping("/{book_memory_wish_id}")
    public BaseResponse modify(
            @Parameter(description = DescriptionUtils.ID_BOOK_MEMORY_WISH) @PathVariable(name = "book_memory_wish_id") Long bookMemoryWishId,
            @RequestBody BookMemoryWishValue.Req.Modification req) {
        Long userId = BasicUtils.getUserId();
        bookMemoryWishService.modify(userId, bookMemoryWishId, req);
        return BaseResponse.SUCCESS_INSTANCE;
    }

    @Operation(summary = "읽고 싶은 책 삭제")
    @DeleteMapping("/{book_memory_wish_id}")
    public BaseResponse delete(
            @Parameter(description = DescriptionUtils.ID_BOOK_MEMORY_WISH) @PathVariable(name = "book_memory_wish_id") Long bookMemoryWishId) {
        Long userId = BasicUtils.getUserId();
        bookMemoryWishService.delete(userId, bookMemoryWishId);
        return BaseResponse.SUCCESS_INSTANCE;
    }
}
