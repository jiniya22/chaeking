package com.chaeking.api.domain.value;

import com.chaeking.api.domain.enumerate.Sex;
import com.chaeking.api.util.DescriptionUtils;
import com.chaeking.api.util.RegexpUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public final class UserValue {
    public final static class Req {
        @Builder
        @Schema(name = "UserCreation")
        public record Creation(
                @Schema(description = "이메일", example = "jini@chaeking.com") @NotBlank @Email String email,
                @Schema(description = DescriptionUtils.PASSWORD) @NotBlank String password,
                @Schema(description = DescriptionUtils.SECRET_KEY) @Length(min = 32) String secretKey,
                @Schema(description = "닉네임") @NotBlank String nickname,
                @Schema(description = "성별") @Pattern(regexp = RegexpUtils.SEX) String sex,
                @Schema(description = "푸시 동의") boolean push,
                @Schema(description = "야간 푸시 동의") boolean nightPush) { }

        @Schema(name = "UserLogin")
        public record Login(
                @Schema(description = "이메일", example = "jini@chaeking.com") @NotBlank @Email String email,
                @Schema(description = DescriptionUtils.PASSWORD) @NotBlank String password,
                @Schema(description = DescriptionUtils.SECRET_KEY) @Length(min = 32) String secretKey) { }

        @Schema(name = "UserModification")
        public record Modification(
                @Schema(description = "이메일", example = "jini@chaeking.com") @Email String email,
                @Schema(description = "닉네임") String nickname) { }
    }

    public final static class Res {
        @Schema(name = "UserDetail")
        public record Detail(String email, String nickname, Sex sex) { }
    }
}
