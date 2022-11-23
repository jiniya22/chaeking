package com.chaeking.api.faq.adapter.in.web;

import com.chaeking.api.common.DataResponse;
import com.chaeking.api.common.annotation.WebAdapter;
import com.chaeking.api.faq.application.port.in.GetFaqQuery;
import com.chaeking.api.faq.application.port.out.FaqDetail;
import com.chaeking.api.faq.application.port.out.FaqSimple;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@WebAdapter
@SecurityRequirements
@RequiredArgsConstructor
@Tag(name = "setting-system", description = "설정-시스템(공지사항, FAQ, 이용약관, 메타정보 등)")
@RestController
@RequestMapping("/v1/faqs")
class GetFaqController {

    private final GetFaqQuery getFaqQuery;

    @Operation(summary = "FAQ(자주묻는 질문) 목록")
    @GetMapping("")
    public DataResponse<List<FaqSimple>> notices(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {

        return DataResponse.create(
                getFaqQuery.getFaqs(PageRequest.of(page, size, Sort.by(Sort.Order.desc("id")))));
    }

    @Operation(summary = "FAQ(자주묻는 질문) 상세보기")
    @GetMapping("/{faq_id}")
    public DataResponse<FaqDetail> faq(
            @Parameter(description = "FAQ id") @PathVariable(name = "faq_id") long faqId) {
        return DataResponse.create(getFaqQuery.getFaq(faqId));
    }

}
