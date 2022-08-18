package com.chaeking.api.controller.v1;

import com.chaeking.api.domain.value.BoardValue;
import com.chaeking.api.domain.value.response.DataResponse;
import com.chaeking.api.service.FaqService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirements
@RequiredArgsConstructor
@Tag(name = "setting-system", description = "설정-시스템(공지사항, FAQ, 이용약관, 메타정보 등)")
@RestController
@RequestMapping("/v1/faqs")
public class FaqController {
    private final FaqService faqService;

    @Operation(summary = "FAQ(자주묻는 질문) 목록")
    @GetMapping("")
    public DataResponse<List<BoardValue.Res.Simple>> faqs(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        List<BoardValue.Res.Simple> data = faqService.faqs(PageRequest.of(page, size, Sort.by(Sort.Order.desc("id"))));
        return DataResponse.of(data);
    }

    @Operation(summary = "FAQ(자주묻는 질문) 상세보기")
    @GetMapping("/{faq_id}")
    public DataResponse<BoardValue.Res.Detail> notice(
            @Parameter(description = "FAQ id") @PathVariable(name = "faq_id") long faqId) {
        return DataResponse.of(faqService.faq(faqId));
    }

}
