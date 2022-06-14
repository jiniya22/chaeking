package com.chaeking.api.controller.temp;

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

@Tag(name = "temp", description = "(테스트용) 암호화, 네이버 책 검색")
@RequiredArgsConstructor
@RequestMapping("/temp/book")
@RestController
public class BookSearchController {

    private final BookService bookService;

    @Operation(summary = "네이버 책 기본 검색",
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
    @GetMapping("")
    public DataResponse<NaverBookValue.Res.BookBasic> searchNaverBasic(
            @Parameter(description = "책 이름") @RequestParam @NotBlank String name,
            @Parameter(description = "정렬 옵션") @RequestParam(defaultValue = "sim") String sort) {
        NaverBookValue.Res.BookBasic res = bookService.searchNaverBasic(name, sort);
        return DataResponse.of(res);
    }

}
