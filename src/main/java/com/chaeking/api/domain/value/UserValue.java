package com.chaeking.api.domain.value;

import com.chaeking.api.domain.entity.User;
import com.chaeking.api.domain.enumerate.Sex;
import com.chaeking.api.util.DateTimeUtils;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Optional;

public final class UserValue {
    public final static class Req {
        @Schema(name = "UserCreation")
        public record Creation(
                @Schema(description = "이메일") @NotBlank @Email String email,
                @Schema(description = "비밀번호") @NotBlank String password,
                @Schema(description = "이름") @NotBlank String name,
                @Schema(description = "생년월일(yyyy-MM-dd)") @NotBlank @Pattern(regexp = "^(19[0-9][0-9]|20\\d{2})-(0[0-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])$") String birthDate,
//                @Schema(description = "생년월일(yyyy-MM-dd)") LocalDate birthDate,
                @Schema(description = "성별") @Pattern(regexp = "^[MF]$") Sex sex) { }
    }

    public final static class Res {
        @Schema(name = "UserDetail")
        public record Detail(String email, String name, String birthDate, Sex sex) {
            public final static Detail of(User u) {
                return new Detail(u.getEmail(),
                        u.getName(),
                        Optional.ofNullable(u.getBirthDate()).map(m -> m.format(DateTimeUtils.DATE_FORMATTER)).orElse(null),
                        u.getSex());
            }
        }
    }
}
