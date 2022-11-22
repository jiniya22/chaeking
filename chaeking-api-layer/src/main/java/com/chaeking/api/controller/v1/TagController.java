package com.chaeking.api.controller.v1;

import com.chaeking.api.domain.value.BaseValue;
import com.chaeking.api.domain.value.response.DataResponse;
import com.chaeking.api.service.TagService;
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
@Tag(name = "tag", description = "태그")
@RestController
@RequestMapping("/v1/tags")
public class TagController {

    private final TagService tagService;

    @Operation(summary = "태그 목록")
    @GetMapping("")
    public DataResponse<List<BaseValue>> tags() {
        return DataResponse.of(tagService.tags());
    }
}
