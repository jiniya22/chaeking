package com.chaeking.api.controller.temp;

import com.chaeking.api.model.enumerate.KakaoBookSort;
import com.chaeking.api.model.enumerate.KakaoBookTarget;
import com.chaeking.api.model.BookValue;
import com.chaeking.api.model.naver.NaverBookValue;
import com.chaeking.api.model.response.DataResponse;
import com.chaeking.api.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;

@SecurityRequirements
@Tag(name = "temp", description = "(테스트용) 암호화, 네이버 책검색, 메일 전송")
@RequiredArgsConstructor
@RequestMapping("/temp")
@RestController
public class BookSearchController {

    private final BookService bookService;

    @Operation(summary = "네이버 책 기본 검색 및 저장",
            description = """
                    <ul>
                        <li>page: 0 ~ 999 사이의 값 (default: 0)</li>
                        <li>size: 1 ~ 50 사이의 값 (default: 10)</li>
                        <li>정렬 옵션
                            <ul>
                                <li>sim: 유사도순</li>
                                <li>date: 출간일순</li>
                                <li>count: 판매량순</li>
                            </ul>
                        </li>
                    </ul>
                    """
    )
    @GetMapping("/book-naver")
    public DataResponse<List<BookValue.Res.Simple>> searchNaverBookBasic(
            @Parameter(description = "책 이름") @RequestParam @NotBlank String name,
            @Parameter(description = "정렬 옵션") @RequestParam(defaultValue = "sim") String sort,
            @RequestParam(required = false, defaultValue = "0") @Min(0) @Max(999) int page,
            @RequestParam(required = false, defaultValue = "10") @Min(1) @Max(100) int size) {
        NaverBookValue.Req.Search naverBookSearch = NaverBookValue.Req.Search.builder()
                .query(name).sort(sort).start(page + 1).display(size).build();
        List<Long> bookIds = bookService.searchNaverBookBasic(naverBookSearch);
        return DataResponse.of(bookService.selectAll(bookIds));
    }

    @Operation(summary = "테스트용"
    )
    @GetMapping("/books")
    public DataResponse<List<BookValue.Res.Simple>> searchKakaoBook(
            @Parameter(description = "검색어") @RequestParam(required = false) String query,
            @Parameter(description = "검색 필드 제한") @RequestParam(required = false) KakaoBookTarget target,
            @Parameter(description = "정렬 옵션") @RequestParam(required = false) KakaoBookSort sort,
            @RequestParam(value = "page", required = false, defaultValue = "0") @Min(0) @Max(100) int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") @Min(1) @Max(50) int size) {
        List<BookValue.Res.Simple> res = bookService.searchTemp(query, target, sort, page + 1, size);
        return DataResponse.of(res);
    }
}
