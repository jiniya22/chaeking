package com.chaeking.api.user.application.port.out;

import io.swagger.v3.oas.annotations.media.Schema;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public record UserLogin(
        @Schema(description = "이메일", example = "jini@chaeking.com") @NotBlank @Email String email,
        @Schema(description = "secret_key 로 AES 암호화한 비밀번호") @NotBlank String password,
        @Schema(description = "uuid 또는 난수 (32자 이상)") @Length(min = 32) String secretKey
) {
}
