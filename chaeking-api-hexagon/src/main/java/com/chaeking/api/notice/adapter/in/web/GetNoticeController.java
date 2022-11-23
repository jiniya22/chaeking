package com.chaeking.api.notice.adapter.in.web;

import com.chaeking.api.common.DataResponse;
import com.chaeking.api.common.annotation.WebAdapter;
import com.chaeking.api.notice.application.port.out.NoticeDetail;
import com.chaeking.api.notice.application.port.in.GetNoticeQuery;
import com.chaeking.api.notice.application.port.out.NoticeSimple;
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
@RequestMapping("/v1/notices")
class GetNoticeController {

    private final GetNoticeQuery getNoticeQuery;

    @Operation(summary = "공지사항 목록")
    @GetMapping("")
    public DataResponse<List<NoticeSimple>> notices(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {

        return DataResponse.create(
                getNoticeQuery.getNoticeSimples(PageRequest.of(page, size, Sort.by(Sort.Order.desc("id")))));
    }

    @Operation(summary = "공지사항 상세보기")
    @GetMapping("/{notice_id}")
    public DataResponse<NoticeDetail> notice(
            @Parameter(description = "공지사항 id") @PathVariable(name = "notice_id") long noticeId) {
        return DataResponse.create(getNoticeQuery.getNoticeDetail(noticeId));
    }
}
