package com.chaeking.api.controller.v1;

import com.chaeking.api.domain.dto.data.BookMemoryDto;
import com.chaeking.api.domain.dto.response.BaseResponse;
import com.chaeking.api.service.BookMemoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "book-memory", description = "북 메모리(이미 읽은 책, 읽고 싶은책)")
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/bookMemories")
public class BookMemoryController {

    private final BookMemoryService bookMemoryService;

    @Operation(summary = "이미 읽은 책 등록")
    @PostMapping("/complete")
    public BaseResponse insertBookMemoryComplete(
            @RequestHeader(name = "X-Chaeking-User-Id") Long chaekingUserId,
            @RequestBody BookMemoryDto.BookMemoryCompleteReq req) {
        bookMemoryService.insertBookMemoryComplete(chaekingUserId, req);
        return new BaseResponse();
    }

    @Operation(summary = "읽고 싶은 책 등록")
    @PostMapping("/wish")
    public BaseResponse insertBookMemoryWish(
            @RequestHeader(name = "X-Chaeking-User-Id") Long chaekingUserId,
            @RequestBody BookMemoryDto.BookMemoryWishReq req) {
        bookMemoryService.insertBookMemoryWish(chaekingUserId, req);
        return new BaseResponse();
    }
}
