package com.chaeking.api.controller.test;

import com.chaeking.api.domain.value.UserValue;
import com.chaeking.api.util.DescriptionUtils;
import com.chaeking.api.util.cipher.AESCipher;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Tag(name = "test", description = "(테스트) 암호화")
@RestController
@RequestMapping("/test/cipher")
public class CipherController {

    @Operation(summary = "AES Cipher Test")
    @PostMapping("/aes")
    public UserValue.Req.Login aesTest(@RequestBody @Valid TestCipherReq req) {
        String password = AESCipher.encrypt(req.password(), req.secretKey());
        return new UserValue.Req.Login(req.email(), password, req.secretKey());
    }

    public record TestCipherReq(
        @Schema(description = "이메일", example = "jini@chaeking.com") @NotBlank @Email String email,
        @Schema(description = DescriptionUtils.PASSWORD) @NotBlank String password,
        @Schema(example = "A37aXdxH6gwTySajLe8eZWNvyC2yuZVB") @Length(min = 32) String secretKey) {
    }
}
