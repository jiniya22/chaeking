package com.chaeking.api.controller.v1;

import com.chaeking.api.value.TokenValue;
import com.chaeking.api.value.UserValue;
import com.chaeking.api.value.response.BaseResponse;
import com.chaeking.api.value.response.DataResponse;
import com.chaeking.api.service.UserService;
import com.chaeking.api.util.BasicUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;

@SecurityRequirements
@RequiredArgsConstructor
@Tag(name = "auth", description = "인증(토큰, 회원 가입)")
@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    private final UserService userService;

    @Operation(summary = "회원 가입",
            description = """
                    <ul>
                        <li>secret_key: uuid 나 32자 이상의 난수값</li>
                        <li>password: secret_key 를 이용하여 비밀번호를 AES 암호화한 값</li>
                        <li>push, night_push: 푸시 동의, 야간 푸시 동의. 옵션값으로, 미설정시 false 로 간주합니다.</li>
                    </ul>
                    <b>※ 필수 약관은 반드시 동의해야 가입할 수 있으므로, Request Body 로 별도로 받지 않습니다.</b>
                    """,
            responses = @ApiResponse(responseCode = "201"))
    @PostMapping("/join")
    public ResponseEntity<BaseResponse> save(@RequestBody @Valid UserValue.Req.Creation req) {
        userService.save(req);
        return ResponseEntity
                .created(ServletUriComponentsBuilder.fromCurrentContextPath()
                        .pathSegment("v1", "users").build().toUri())
                .body(BaseResponse.SUCCESS_INSTANCE);
    }

    // Swagger 문서화를 위해 만든 껍데기 메서드
    @Operation(summary = "토큰 발급",
            description = """
                    email 과 password 또는 refresh_token을 이용하여 토큰을 발급합니다.<br>
                    발급 성공시 access_token, refresh_token 을 리턴해줍니다.<br>
                    <ul>
                        <li>secret_key: uuid 나 32자 이상의 난수값</li>
                        <li>password: secret_key 를 이용하여 비밀번호를 AES 암호화한 값</li>
                    </ul>
                    """)
    @PostMapping("/token")
    public DataResponse<TokenValue.Token> login(
            @RequestHeader(value = "X-Refresh-Token", required = false) String refreshToken,
            @RequestBody(required = false) UserValue.Req.Login req) {
        return DataResponse.of(new TokenValue.Token(req.email(), refreshToken));
    }

    @SecurityRequirement(name = "access_token")
    @Operation(summary = "토큰 철회",
            description = "refresh_token을 폐기합니다.<br>")
    @DeleteMapping("/revoke")
    public BaseResponse revoke() {
        Long userId = BasicUtils.getUserId();
        userService.revoke(userId);
        return BaseResponse.SUCCESS_INSTANCE;
    }
}
