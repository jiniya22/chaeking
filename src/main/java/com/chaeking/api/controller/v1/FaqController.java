package com.chaeking.api.controller.v1;

import com.chaeking.api.domain.dto.data.BoardDto;
import com.chaeking.api.domain.dto.response.CommonResponse;
import com.chaeking.api.service.FaqService;
import com.chaeking.api.service.NoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@Tag(name = "setting-system", description = "세팅-시스템(공지사항, FAQ, 이용약관 등)")
@RestController
@RequestMapping("/v1/faqs")
public final class FaqController {
    private final FaqService faqService;

    @Operation(summary = "FAQ(자주묻는 질문) 목록")
    @GetMapping("")
    public CommonResponse<List<BoardDto>> notices(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        List<BoardDto> data = faqService.faqs(PageRequest.of(page, size, Sort.by(Sort.Order.desc("id"))));
        return new CommonResponse<>(data);
    }
}
