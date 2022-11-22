package com.chaeking.api.controller.v1.setting.system;

import com.chaeking.api.domain.enumerate.MetaType;
import com.chaeking.api.domain.value.response.DataResponse;
import com.chaeking.api.service.MetaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SecurityRequirements
@RequiredArgsConstructor
@Tag(name = "setting-system", description = "설정-시스템(공지사항, FAQ, 이용약관, 메타정보 등)")
@RestController
@RequestMapping("/v1/meta")
public class MetaController {

    private final MetaService metaService;

    @Operation(summary = "메타 정보(앱 버전)")
    @GetMapping("")
    public DataResponse<String> notices(
            @RequestParam(value = "메타 타입", required = false, defaultValue = "AOS_APP_VERSION") MetaType type) {
        return DataResponse.of(metaService.meta(type));
    }

}
