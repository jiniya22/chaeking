package com.chaeking.api.controller.v1;

import com.chaeking.api.domain.dto.data.BaseDto;
import com.chaeking.api.domain.dto.response.CommonResponse;
import com.chaeking.api.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "tag", description = "태그")
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/tags")
public class TagController {

    private final TagService tagService;

    @Operation(summary = "태그 목록")
    @GetMapping("")
    public CommonResponse<List<BaseDto>> tags() {
        List<BaseDto> data = tagService.tags();
        return new CommonResponse<>(data);
    }
}
