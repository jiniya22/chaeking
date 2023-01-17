package com.chaeking.api.controller.v1.setting.system;

import com.chaeking.api.model.BoardValue;
import com.chaeking.api.model.ContactValue;
import com.chaeking.api.model.response.BaseResponse;
import com.chaeking.api.model.response.DataResponse;
import com.chaeking.api.model.response.PageResponse;
import com.chaeking.api.service.ContactService;
import com.chaeking.api.util.BasicUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RequiredArgsConstructor
@Tag(name = "setting-system", description = "설정-시스템(공지사항, FAQ, 이용약관, 문의, 메타정보 등)")
@RestController
@RequestMapping("/v1/contact")
public class ContactController {

    private final ContactService contactService;

    @Operation(summary = "문의 목록 조회")
    @GetMapping("")
    public PageResponse<BoardValue.Res.Simple> selectAll(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        Long userId = BasicUtils.getUserId();
        return contactService.selectAll(userId, PageRequest.of(page, size, Sort.by(Sort.Order.desc("id"))));
    }
    
    @Operation(summary = "문의하기(= 문의 추가)", responses = @ApiResponse(responseCode = "201"))
    @PostMapping("")
    public ResponseEntity<BaseResponse> insert(
            @RequestBody BoardValue.Req.Creation req) {
        Long userId = BasicUtils.getUserId();
        Long contactId = contactService.insert(userId, req);
        return ResponseEntity
                .created(ServletUriComponentsBuilder.fromCurrentRequest()
                        .path("/{contact_id}").buildAndExpand(contactId).toUri())
                .body(BaseResponse.SUCCESS_INSTANCE);
    }

    @Operation(summary = "문의 상세보기")
    @GetMapping("/{contact_id}")
    public DataResponse<ContactValue.Res.Detail> selectOne(@Parameter(description = "문의 id") @PathVariable(name = "contact_id") long contactId) {
        Long userId = BasicUtils.getUserId();
        return DataResponse.of(contactService.selectOne(contactId, userId));
    }

}
