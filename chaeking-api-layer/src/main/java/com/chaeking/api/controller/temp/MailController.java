package com.chaeking.api.controller.temp;

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
@Tag(name = "temp", description = "(테스트용) 암호화, 네이버 책검색, 메일 전송")
@RestController
@RequestMapping("/temp/mail")
public class MailController {

    private final MailService mailService;

    @Operation(summary = "메일 전송")
    @PostMapping("/basic")
    public BaseResponse sendMail(@RequestBody @Valid MailValue.MailRequest req) {
        mailService.sendMail(req);
        return BaseResponse.SUCCESS_INSTANCE;
    }

}
