package com.chaeking.api.domain.value;

import com.chaeking.api.domain.enumerate.Sex;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public record Ttt(
        @Schema(description = "이메일") @NotBlank @Email String email,
        @Schema(description = "비밀번호") @NotBlank String password,
        @Schema(description = "이름") @NotBlank String name,
//        @Schema(description = "생년월일(yyyy-MM-dd)") @NotBlank @Pattern(regexp = "^(19[0-9][0-9]|20\\d{2})-(0[0-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])$") String birthDate,
//                @Schema(description = "생년월일(yyyy-MM-dd)") LocalDate birthDate,
        @Schema(description = "성별") Sex sex
) {
}