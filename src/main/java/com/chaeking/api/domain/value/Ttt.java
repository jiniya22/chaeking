package com.chaeking.api.domain.value;

import com.chaeking.api.domain.enumerate.Sex;
import com.chaeking.api.util.RegexpUtils;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public record Ttt(
        @Schema(description = "이메일") @NotBlank @Email String email,
        @Schema(description = "비밀번호") @NotBlank String password,
        @Schema(description = "이름") @Pattern(regexp = "^[a-zA-Z가-힣]{1,10}$") String name,
        @Schema(description = "생년월일(yyyy-MM-dd)") @NotBlank @Pattern(regexp = RegexpUtils.DATE) String birthDate,
        @Pattern(regexp = RegexpUtils.DATETIME) String dateTime,
        @Schema(description = "성별") Sex sex
) {
}