package com.chaeking.api.controller.v1;

import com.chaeking.api.domain.dto.data.BoardDto;
import com.chaeking.api.domain.dto.response.CommonResponse;
import com.chaeking.api.service.NoticeService;
import io.swagger.v3.oas.annotations.Operation;
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

@Tag(name = "setting-system", description = "세팅-시스템(공지사항, FAQ, 이용약관 등)")
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/notices")
public class NoticeController {

    private final NoticeService noticeService;

    @Operation(summary = "공지사항 목록")
    @GetMapping("")
    public CommonResponse<List<BoardDto>> notices(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        List<BoardDto> data = noticeService.notices(PageRequest.of(page, size, Sort.by(Sort.Order.desc("id"))));
        return new CommonResponse<>(data);
    }
}
