package com.chaeking.api.controller.v1;

import com.chaeking.api.domain.dto.data.BookMemoryDto;
import com.chaeking.api.domain.dto.response.BaseResponse;
import com.chaeking.api.service.BookMemoryService;
import com.chaeking.api.util.DescriptionUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@Tag(name = "book-memory", description = "북 메모리(이미 읽은 책, 읽고 싶은책)")
@RestController
@RequestMapping("/v1/bookMemories")
public record BookMemoryController(BookMemoryService bookMemoryService) {

    @Operation(summary = "이미 읽은 책 등록")
    @PostMapping("/complete")
    public BaseResponse insertBookMemoryComplete(
            @Parameter(name = "X-User-Id", description = DescriptionUtils.ID_USER, in = ParameterIn.HEADER)
            @RequestHeader(name = "X-User-Id") Long userId,
            @RequestBody BookMemoryDto.BookMemoryCompleteNewReq req) {
        bookMemoryService.insertBookMemoryComplete(userId, req);
        return new BaseResponse();
    }

    @Operation(summary = "이미 읽은 책 수정")
    @PutMapping("/complete/{bookMemoryCompleteId}")
    public BaseResponse modifyBookMemoryComplete(
            @Parameter(name = "X-User-Id", description = DescriptionUtils.ID_USER, in = ParameterIn.HEADER)
            @RequestHeader(name = "X-User-Id") Long userId,
            @Parameter(name = "bookMemoryCompleteId", description = DescriptionUtils.ID_BOOK_MEMORY_COMPLETE, in = ParameterIn.PATH)
            @PathVariable(name = "bookMemoryCompleteId") Long bookMemoryCompleteId,
            @RequestBody BookMemoryDto.BookMemoryCompleteReq req) {
        bookMemoryService.modifyBookMemoryComplete(userId, bookMemoryCompleteId, req);
        return new BaseResponse();
    }

    @Operation(summary = "이미 읽은 책 삭제")
    @DeleteMapping("/complete/{bookMemoryCompleteId}")
    public BaseResponse deleteBookMemoryComplete(
            @Parameter(name = "X-User-Id", description = DescriptionUtils.ID_USER, in = ParameterIn.HEADER)
            @RequestHeader(name = "X-User-Id") Long userId,
            @Parameter(name = "bookMemoryCompleteId", description = DescriptionUtils.ID_BOOK_MEMORY_COMPLETE, in = ParameterIn.PATH)
            @PathVariable(name = "bookMemoryCompleteId") Long bookMemoryCompleteId) {
        bookMemoryService.deleteBookMemoryComplete(userId, bookMemoryCompleteId);
        return new BaseResponse();
    }

    @Operation(summary = "읽고 싶은 책 등록")
    @PostMapping("/wish")
    public BaseResponse insertBookMemoryWish(
            @Parameter(name = "X-User-Id", description = DescriptionUtils.ID_USER, in = ParameterIn.HEADER)
            @RequestHeader(name = "X-User-Id") Long userId,
            @RequestBody BookMemoryDto.BookMemoryWishNewReq req) {
        bookMemoryService.insertBookMemoryWish(userId, req);
        return new BaseResponse();
    }

    @Operation(summary = "읽고 싶은 책 수정")
    @PutMapping("/complete/{bookMemoryWishId}")
    public BaseResponse modifyBookMemoryWish(
            @Parameter(name = "X-User-Id", description = DescriptionUtils.ID_USER, in = ParameterIn.HEADER)
            @RequestHeader(name = "X-User-Id") Long userId,
            @Parameter(name = "bookMemoryWishId", description = DescriptionUtils.ID_BOOK_MEMORY_WISH, in = ParameterIn.PATH)
            @PathVariable(name = "bookMemoryWishId") Long bookMemoryWishId,
            @RequestBody BookMemoryDto.BookMemoryWishReq req) {
        bookMemoryService.modifyBookMemoryWish(userId, bookMemoryWishId, req);
        return new BaseResponse();
    }

    @Operation(summary = "읽고 싶은 책 삭제")
    @DeleteMapping("/complete/{bookMemoryWishId}")
    public BaseResponse deleteBookMemoryWish(
            @Parameter(name = "X-User-Id", description = DescriptionUtils.ID_USER, in = ParameterIn.HEADER)
            @RequestHeader(name = "X-User-Id") Long userId,
            @Parameter(name = "bookMemoryWishId", description = DescriptionUtils.ID_BOOK_MEMORY_WISH, in = ParameterIn.PATH)
            @PathVariable(name = "bookMemoryWishId") Long bookMemoryWishId) {
        bookMemoryService.deleteBookMemoryWish(userId, bookMemoryWishId);
        return new BaseResponse();
    }
}
