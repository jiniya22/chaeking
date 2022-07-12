package com.chaeking.api.controller.v1;

import com.chaeking.api.domain.value.BookValue;
import com.chaeking.api.domain.value.response.DataResponse;
import com.chaeking.api.service.NewBookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@Tag(name = "book", description = "책, 베스트셀러, 신간")
@RestController
@RequestMapping("/v1/new-books")
public class NewBookController {

    private final NewBookService newBookService;

    @Operation(summary = "신간 목록 조회",
        description = "신간 목록을 조회합니다.<br>" +
                "Kakao API 로 부터 검색되지 않거나 미성년자 구입 불가한 책은 제외하여 조회됩니다. (최대 20개)")
    @GetMapping("")
    public DataResponse<List<BookValue.Res.Simple>> bestSellerTop10() {
        return DataResponse.of(newBookService.newBooks());
    }

}
