package com.chaeking.api.controller.v1;

import com.chaeking.api.domain.enumerate.KakaoBookSort;
import com.chaeking.api.domain.enumerate.KakaoBookTarget;
import com.chaeking.api.domain.value.BookValue;
import com.chaeking.api.domain.value.response.DataResponse;
import com.chaeking.api.service.BookService;
import com.chaeking.api.util.BasicUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;

@RequiredArgsConstructor
@Tag(name = "book", description = "책, 베스트셀러, 신간")
@RestController
@RequestMapping("/v1/books")
public class BookController {

    private final BookService bookService;

    @SecurityRequirements
    @Operation(summary = "책 목록 조회",
            description = """
                    카카오 API 를 이용하여 책 목록을 조회합니다.
                    <ul>
                        <li>page: 0 ~ 99 사이의 값 (default: 0)</li>
                        <li>size: 1 ~ 50 사이의 값 (default: 10)</li>
                        <li>검색 필드 제한
                            <ul>
                                <li>title: 제목</li>
                                <li>isbn: isbn</li>
                                <li>publisher: 출판사</li>
                                <li>person: 인명</li>
                            </ul>
                        </li>
                        <li>정렬 옵션
                            <ul>
                                <li>accuracy: 정확도순</li>
                                <li>latest: 발간일순</li>
                            </ul>
                        </li>
                    </ul>
                    """
    )
    @GetMapping("")
    public DataResponse<List<BookValue.Res.Simple>> searchKakaoBook(
            @Parameter(description = "검색어") @RequestParam @NotBlank String query,
            @Parameter(description = "검색 필드 제한") @RequestParam(required = false) KakaoBookTarget target,
            @Parameter(description = "정렬 옵션") @RequestParam(defaultValue = "accuracy") KakaoBookSort sort,
            @RequestParam(value = "page", required = false, defaultValue = "0") @Min(0) @Max(100) int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") @Min(1) @Max(50) int size) {
        List<Long> bookIds = bookService.searchKakaoBook(query, target, sort, page + 1, size);
        return DataResponse.of(bookService.selectAll(bookIds));
    }

    @Operation(summary = "책 상세조회",
            description = "Authorization 헤더 설정시, 사용자가 설정한 이미 읽은 책, 읽고 싶은 책 정보를 확인할 수 있습니다.")
    @GetMapping("/{book_id}")
    public DataResponse<BookValue.Res.Detail> selectAll(
            @Parameter(description = "책 id") @PathVariable(name = "book_id") long bookId) {
        Long userId = BasicUtils.getUserId();
        return DataResponse.of(bookService.book(bookId, userId));
    }
}
