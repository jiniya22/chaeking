package com.chaeking.api.notice.adapter.in.web;

import com.chaeking.api.notice.application.port.in.GetNoticeQuery;
import com.chaeking.api.notice.domain.Notice;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SecurityRequirements
@RequiredArgsConstructor
@Tag(name = "setting-system", description = "설정-시스템(공지사항, FAQ, 이용약관, 메타정보 등)")
@RestController
@RequestMapping("/v1/notices")
class LoadNoticeController {

    private final GetNoticeQuery getNoticeQuery;

    @Operation(summary = "공지사항 목록")
    @GetMapping("")
    public List<Notice> notices(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {

        return getNoticeQuery.getNotices(PageRequest.of(page, size, Sort.by(Sort.Order.desc("id"))));
    }
}
