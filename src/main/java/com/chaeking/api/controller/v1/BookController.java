package com.chaeking.api.controller.v1;

import com.chaeking.api.domain.enumerate.KakaoBookSort;
import com.chaeking.api.domain.enumerate.KakaoBookTarget;
import com.chaeking.api.domain.value.BookValue;
import com.chaeking.api.domain.value.naver.KakaoBookValue;
import com.chaeking.api.domain.value.response.DataResponse;
import com.chaeking.api.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.Calendar;

@RequiredArgsConstructor
@Tag(name = "book", description = "책")
@RestController
@RequestMapping("/v1/books")
public class BookController {

    private final BookService bookService;

    @Operation(summary = "책 목록 조회",
            description = """
                    카카오 API 를 이용하여 책 목록을 조회합니다.
                    <ul>
                        <li>page: 1~100 사이의 값 (default: 1)</li>
                        <li>size: 1~50 사이의 값 (default: 10)</li>
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
    public DataResponse<KakaoBookValue.Res.BookBasic> searchKakaoBook(
            @Parameter(description = "검색어") @RequestParam @NotBlank String query,
            @Parameter(description = "검색 필드 제한") @RequestParam(required = false) KakaoBookTarget target,
            @Parameter(description = "정렬 옵션") @RequestParam(defaultValue = "accuracy") KakaoBookSort sort,
            @RequestParam(value = "page", required = false, defaultValue = "1") @Min(1) @Max(100) int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") @Min(1) @Max(50) int size) {
        KakaoBookValue.Res.BookBasic res = bookService.searchKakaoBook(query, target, sort, page, size);
        return DataResponse.of(res);
    }

    @Operation(summary = "책 상세조회")
    @GetMapping("/{book_id}")
    public DataResponse<BookValue.Res.Detail> selectAll(
            @Parameter(description = "책 id") @PathVariable(name = "book_id") long bookId) {
        BookValue.Res.Detail data = bookService.book(bookId);
        Calendar cal = Calendar.getInstance();
        return DataResponse.of(data);
    }
}
