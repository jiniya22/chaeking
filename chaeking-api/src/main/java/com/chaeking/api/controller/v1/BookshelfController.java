package com.chaeking.api.controller.v1;

import com.chaeking.api.domain.value.BookMemoryCompleteValue;
import com.chaeking.api.domain.value.response.PageResponse;
import com.chaeking.api.service.BookshelfService;
import com.chaeking.api.util.BasicUtils;
import com.chaeking.api.util.DescriptionUtils;
import com.chaeking.api.util.RegexpUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

@RequiredArgsConstructor
@Tag(name = "bookshelf", description = "책장")
@RestController
@RequestMapping("/v1/bookshelf")
public class BookshelfController {

    private final BookshelfService bookshelfService;
    private static final int BOOKSHELF_SIZE = 9;

    @GetMapping("")
    @Operation(summary = "사용자별 책장 조회",
            description = """
                    - month: yyyyMM 형태로 입력. 입력하지 않은 경우 오늘 날짜를 이용합니다
                    - page: 0 ~ 999 사이의 값. 기본값은 0
                    - **size 는 9로 고정**
                    - **Authorization 헤더 필수**
                    """)
    public PageResponse<BookMemoryCompleteValue.Res.Bookshelf> bookshelf(
            @Parameter(description = DescriptionUtils.MONTH) @RequestParam(required = false) @Pattern(regexp = RegexpUtils.MONTH) String month,
            @RequestParam(value = "page", required = false, defaultValue = "0") @Min(0) @Max(999) int page
    ) {
        Long userId = BasicUtils.getUserId();
        return bookshelfService.bookshelf(userId, month, PageRequest.of(page, BOOKSHELF_SIZE, Sort.by(Sort.Order.desc("id"))));
    }
}
