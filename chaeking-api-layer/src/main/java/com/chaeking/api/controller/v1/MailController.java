package com.chaeking.api.controller.v1;

import com.chaeking.api.model.MailValue;
import com.chaeking.api.model.response.BaseResponse;
import com.chaeking.api.service.MailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@SecurityRequirements
@Tag(name = "mail", description = "메일 전송")
@RestController
@RequestMapping("/v1/mail")
public class MailController {

    private final MailService mailService;

    @Operation(summary = "(테스트용) 메일 전송")
    @PostMapping("/basic")
    public BaseResponse sendMail(@RequestBody @Valid MailValue.MailRequest req) {
        mailService.sendMail(req);
        return BaseResponse.SUCCESS_INSTANCE;
    }

    @Operation(summary = "비밀번호 변경 이메일 전송",
            description = """
                    임시 비밀번호를 전송합니다.<br>
                    회원가입된 이메일이 아닐 경우 400 에러를 발생
                    """)
    @PostMapping("/password-issue")
    public BaseResponse sendTemporaryPasswordMail(@RequestBody @Valid MailValue.MailTemporaryPasswordRequest req) {
        mailService.sendTemporaryPassword(req);
        return BaseResponse.SUCCESS_INSTANCE;
    }

}
