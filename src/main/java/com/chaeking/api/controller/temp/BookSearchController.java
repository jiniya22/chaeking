package com.chaeking.api.controller.temp;

import com.chaeking.api.domain.value.naver.KakaoBookValue;
import com.chaeking.api.domain.value.naver.NaverBookValue;
import com.chaeking.api.domain.value.response.DataResponse;
import com.chaeking.api.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;

@Tag(name = "temp", description = "(테스트용) 암호화, 네이버/카카오 책 검색")
@RequiredArgsConstructor
@RequestMapping("/temp")
@RestController
public class BookSearchController {

    private final BookService bookService;

    @Operation(summary = "네이버 책 기본 검색 및 저장",
            description = """
                    <ul>
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
    public DataResponse<NaverBookValue.Res.BookBasic> searchNaverBookBasic(
            @Parameter(description = "책 이름") @RequestParam @NotBlank String name,
            @Parameter(description = "정렬 옵션") @RequestParam(defaultValue = "sim") String sort) {
        NaverBookValue.Res.BookBasic res = bookService.searchNaverBookBasic(name, sort);
        return DataResponse.of(res);
    }

    @Operation(summary = "카카오 책 검색 및 저장",
            description = """
                    <ul>
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
    @GetMapping("/book-kakao")
    public DataResponse<KakaoBookValue.Res.BookBasic> searchKakaoBook(
            @Parameter(description = "검색어") @RequestParam @NotBlank String query,
            @Parameter(description = "검색 필드 제한") @RequestParam String target,
            @Parameter(description = "정렬 옵션") @RequestParam(defaultValue = "accuracy") String sort) {
        KakaoBookValue.Res.BookBasic res = bookService.searchKakaoBook(query, target, sort);
        return DataResponse.of(res);
    }
}
