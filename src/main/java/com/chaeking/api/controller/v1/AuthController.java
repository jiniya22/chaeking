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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@Tag(name = "auth", description = "인증(로그인, 회원 가입, 회원 정보 조회)")
@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    private final UserService userService;

    @Operation(summary = "회원 가입",
            description = "<ul><li>secret_key: uuid 나 32자 이상의 난수값</li>" +
                    "<li>password: secret_key 를 이용하여 비밀번호를 AES 암호화한 값</li></ul>")
    @PostMapping("/join")
    public BaseResponse save(@RequestBody @Valid UserValue.Req.Creation req) {
        return userService.save(req);
    }

    @Operation(summary = "로그인",
            description = "email 과 password 를 이용하여 로그인합니다.<br>" +
                    "로그인 성공시, user_id 와 access_token, refresh_token 을 리턴해줍니다.<br>" +
                    "<ul><li>secret_key: uuid 나 32자 이상의 난수값</li>" +
                    "<li>password: secret_key 를 이용하여 비밀번호를 AES 암호화한 값</li></ul>")
    @PostMapping("/login")
    public DataResponse<TokenValue.Token> login(
            @RequestHeader(value = "X-Refresh-Token", required = false) String refreshToken,
            @RequestBody(required = false) UserValue.Req.Login req) {
        return DataResponse.of(userService.login(refreshToken, req));
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "회원 정보 조회")
    @GetMapping("/profile")
    public DataResponse<UserValue.Res.Detail> selectOne() {
        long userId = BasicUtils.getUserId();
        return DataResponse.of(userService.selectDetail(userId));
    }

}
