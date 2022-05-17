package com.chaeking.api.domain.value;

import com.chaeking.api.domain.enumerate.Sex;
import io.swagger.v3.oas.annotations.media.Schema;

public record UserDto(@Schema(description = "이메일", required = true) String email,
                      @Schema(description = "비밀번호", required = true) String password,
                      @Schema(description = "이름", required = true) String name,
                      @Schema(description = "생년월일(yyyyMMdd)") String birth_date,
                      @Schema(description = "성별") Sex sex) {
}
