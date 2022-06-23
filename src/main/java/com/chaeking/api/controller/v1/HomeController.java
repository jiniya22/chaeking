package com.chaeking.api.controller.v1;

import com.chaeking.api.domain.enumerate.AnalysisType;
import com.chaeking.api.domain.value.AnalysisValue;
import com.chaeking.api.domain.value.response.DataResponse;
import com.chaeking.api.service.BookshelfService;
import com.chaeking.api.util.BasicUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Tag(name = "home", description = "홈")
@RestController
@RequestMapping("/v1/home")
public class HomeController {

    private final BookshelfService bookshelfService;

    @GetMapping("")
    @Operation(summary = "사용자별 홈 화면 조회",
            description = """
                    - **Authorization 헤더 필수**
                    - type: daily, weekly, monthly 중 하나. 기본값은 daily
                    """)
    public DataResponse<AnalysisValue.BookAnalysis> home(
            @Parameter(description = "조회 기준") @RequestParam(required = false, defaultValue = "daily") AnalysisType type
    ) {
        Long userId = BasicUtils.getUserId();
        return DataResponse.of(bookshelfService.bookAnalysis(userId, type));
    }
    
}
