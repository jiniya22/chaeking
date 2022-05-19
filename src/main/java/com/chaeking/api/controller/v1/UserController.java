package com.chaeking.api.controller.v1;

import com.chaeking.api.domain.value.UserValue;
import com.chaeking.api.domain.value.response.BaseResponse;
import com.chaeking.api.domain.value.response.DataResponse;
import com.chaeking.api.service.UserService;
import com.chaeking.api.util.DescriptionUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@Tag(name = "user", description = "사용자")
@RestController
@RequestMapping("/v1/users")
public final class UserController {

    private final UserService userService;

    @Operation(summary = "사용자 등록(= 회원 가입)")
    @PostMapping("")
    public BaseResponse save(@RequestBody @Valid UserValue.Req.Creation req) {
        BaseResponse res = userService.save(req);
        return res;
    }

    @Operation(summary = "사용자 조회")
    @GetMapping("/{userId}")
    public DataResponse<UserValue.Res.Detail> selectOne(@Parameter(description = DescriptionUtils.ID_USER) @PathVariable("userId") long userId) {
        return DataResponse.of(userService.selectDetail(userId));
    }
}
