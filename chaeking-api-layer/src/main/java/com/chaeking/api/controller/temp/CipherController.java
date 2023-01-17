package com.chaeking.api.controller.temp;

import com.chaeking.api.value.UserValue;
import com.chaeking.api.util.DescriptionUtils;
import com.chaeking.api.util.cipher.AESCipher;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@SecurityRequirements
@Tag(name = "temp", description = "(테스트용) 암호화, 네이버 책검색")
@RestController
@RequestMapping("/temp/cipher")
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
