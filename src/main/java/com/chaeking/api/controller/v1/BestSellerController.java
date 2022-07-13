package com.chaeking.api.controller.v1;

import com.chaeking.api.domain.value.BookValue;
import com.chaeking.api.domain.value.response.DataResponse;
import com.chaeking.api.service.BestSellerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SecurityRequirements
@RequiredArgsConstructor
@Tag(name = "book", description = "책, 베스트셀러, 신간")
@RestController
@RequestMapping("/v1/best-sellers")
public class BestSellerController {

    private final BestSellerService bestSellerService;

    @Operation(summary = "베스트셀러 Top 10 조회",
            description = "베스트셀러 목록을 조회합니다.<br>" +
                    "Kakao API 로 부터 검색되지 않거나 미성년자 구입 불가한 책은 제외하여 조회됩니다. (최대 10개)")
    @GetMapping("")
    public DataResponse<List<BookValue.Res.Simple>> bestSellerTop10() {
        return DataResponse.of(bestSellerService.bestSellers());
    }

}
