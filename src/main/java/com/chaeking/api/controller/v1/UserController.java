package com.chaeking.api.controller.v1;

import com.chaeking.api.domain.value.TokenValue;
import com.chaeking.api.domain.value.UserValue;
import com.chaeking.api.domain.value.response.BaseResponse;
import com.chaeking.api.domain.value.response.DataResponse;
import com.chaeking.api.service.UserService;
import com.chaeking.api.util.BasicUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@Tag(name = "user", description = "회원")
@RestController
@RequestMapping("/v1/users")
public class UserController {

    private final UserService userService;

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "회원 정보 조회")
    @GetMapping("")
    public DataResponse<UserValue.Res.Detail> selectOne() {
        return DataResponse.of(userService.selectDetail(BasicUtils.getUserId()));
    }

    @Operation(summary = "회원 정보 수정 - 이메일 or 닉네임",
            description = """
                    <ul>
                        <li>email 또는 nickname 을 수정합니다.</li>
                        <li>email 은 email 형식에 맞지 않을 경우 Exception 을 발생시킵니다.</li>
                    </ul>
                    <b>※ email 을 수정했을 경우, header 에 담긴 토큰 값(X-Access-Token, X-Refresh-Token)을 이용하여 기존 토큰을 갱신해야합니다.</b>
                    """)
    @PatchMapping("")
    public ResponseEntity<BaseResponse> patchUser(@RequestBody @Valid UserValue.Req.Modification req) {
        TokenValue.Token token = userService.patch(BasicUtils.getUserId(), req);
        ResponseEntity.BodyBuilder builder = ResponseEntity.ok();
        if(token != null) {
            builder.header("X-Access-Token", token.accessToken());
            builder.header("X-Refresh-Token", token.refreshToken());
        }
        return builder.body(BaseResponse.of());
    }

}
