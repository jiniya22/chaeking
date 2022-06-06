package com.chaeking.api.controller.v1;

import com.chaeking.api.domain.value.Ttt;
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

    @Operation(summary = "로그인",
            description = "email 과 password 를 이용하여 로그인합니다.<br>" +
                    "로그인 성공시, user_id 와 access_token, refresh_token 을 리턴해줍니다.<br>" +
                    "<ul><li>secret_key: uuid 나 32자 이상의 난수값</li>" +
                    "<li>password: secret_key 를 이용하여 비밀번호를 AES 암호화한 값</li></ul>")
    @PostMapping("/login")
    public DataResponse<UserValue.Res.Token> login(@RequestBody @Valid UserValue.Req.Login req) {
        UserValue.Res.Token res = userService.login(req);
        return DataResponse.of(res);
    }

    @Operation(summary = "사용자 조회")
    @GetMapping("/{user_id}")
    public DataResponse<UserValue.Res.Detail> selectOne(@Parameter(description = DescriptionUtils.ID_USER) @PathVariable("user_id") long userId) {
        return DataResponse.of(userService.selectDetail(userId));
    }

    @Operation(summary = "(삭제 예정) 테스트")
    @PostMapping("/test")
    public DataResponse<Ttt> test(@RequestBody @Valid Ttt req) {
        System.out.println(req);
        return DataResponse.of(req);
    }
}
