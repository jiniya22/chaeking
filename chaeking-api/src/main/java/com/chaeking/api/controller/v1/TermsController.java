package com.chaeking.api.controller.v1;

import com.chaeking.api.domain.value.TermsValue;
import com.chaeking.api.domain.value.response.DataResponse;
import com.chaeking.api.service.TermsService;
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
@Tag(name = "setting-system", description = "설정-시스템(공지사항, FAQ, 이용약관, 메타정보 등)")
@RestController
@RequestMapping("/v1/terms")
public class TermsController {

    private final TermsService termsService;

    @Operation(summary = "이용 약관 목록")
    @GetMapping("")
    public DataResponse<List<TermsValue>> terms() {
        return DataResponse.of(termsService.terms());
    }
}
