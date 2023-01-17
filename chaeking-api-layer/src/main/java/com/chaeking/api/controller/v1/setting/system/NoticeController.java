package com.chaeking.api.controller.v1.setting.system;

import com.chaeking.api.model.BoardValue;
import com.chaeking.api.model.response.DataResponse;
import com.chaeking.api.service.NoticeService;
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
@RequestMapping("/v1/notices")
public class NoticeController {

    private final NoticeService noticeService;

    @Operation(summary = "공지사항 목록")
    @GetMapping("")
    public DataResponse<List<BoardValue.Res.Simple>> notices(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return DataResponse.of(noticeService.notices(PageRequest.of(page, size, Sort.by(Sort.Order.desc("id")))));
    }

    @Operation(summary = "공지사항 상세보기")
    @GetMapping("/{notice_id}")
    public DataResponse<BoardValue.Res.Detail> notice(
            @Parameter(description = "공지사항 id") @PathVariable(name = "notice_id") long noticeId) {
        return DataResponse.of(noticeService.notice(noticeId));
    }
}
