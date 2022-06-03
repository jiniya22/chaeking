package com.chaeking.api.domain.value;

import com.chaeking.api.domain.entity.User;
import com.chaeking.api.domain.enumerate.Sex;
import com.chaeking.api.util.DateTimeUtils;
import com.chaeking.api.util.RegexpUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Optional;

public final class UserValue {
    public final static class Req {
        @Schema(name = "UserCreation")
        public record Creation(
                @Schema(description = "이메일", example = "jini@chaeking.com") @NotBlank @Email String email,
                @Schema(description = "비밀번호") @NotBlank String password,
                @Schema(description = "이름") @NotBlank String name,
                @Schema(description = "생년월일(yyyy-MM-dd)") @NotBlank @Pattern(regexp = RegexpUtils.DATE) String birthDate,
                @Schema(description = "성별") @Pattern(regexp = RegexpUtils.SEX) String sex) { }

        @Schema(name = "UserLogin")
        public record Login(
                @Schema(description = "이메일", example = "jini@chaeking.com") @NotBlank @Email String email,
                @Schema(description = "비밀번호") @NotBlank String password,
                @Schema(description = "암호화 키(uuid)") @Length(min = 32) String secretKey) { }
    }

    public final static class Res {
        @Schema(name = "UserDetail")
        public record Detail(String email, String name, String birthDate, Sex sex) {
            public final static Detail of(User u) {
                return new Detail(u.getEmail(),
                        u.getName(),
                        Optional.ofNullable(u.getBirthDate()).map(m -> m.format(DateTimeUtils.FORMATTER_DATE)).orElse(null),
                        u.getSex());
            }
        }
    }
}
