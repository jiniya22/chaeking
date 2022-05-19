package com.chaeking.api.controller.v1;

import com.chaeking.api.domain.value.BoardValue;
import com.chaeking.api.domain.value.response.DataResponse;
import com.chaeking.api.service.NoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@Tag(name = "setting-system", description = "세팅-시스템(공지사항, FAQ, 이용약관 등)")
@RestController
@RequestMapping("/v1/notices")
public final class NoticeController {

    private final NoticeService noticeService;

    @Operation(summary = "공지사항 목록")
    @GetMapping("")
    public DataResponse<List<BoardValue.Res.Simple>> notices(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        List<BoardValue.Res.Simple> data = noticeService.notices(PageRequest.of(page, size, Sort.by(Sort.Order.desc("id"))));
        return DataResponse.of(data);
    }
}