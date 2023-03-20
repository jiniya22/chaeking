package com.chaeking.api.model;

import com.chaeking.api.model.enumerate.ReasonCode;
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
                @Schema(description = "닉네임") String nickname,
                @Schema(description = "푸시 동의") Boolean push,
                @Schema(description = "야간 푸시 동의") Boolean nightPush) { }

        @Schema(name = "UserNickname")
        public record Nickname(
                @Schema(description = "닉네임") @NotBlank String nickname) { }

        @Schema(name = "UserEmail")
        public record UserEmail(
                @Schema(description = "닉네임") @NotBlank @Email String email) { }

        @Schema(name = "UserDeativate")
        public record Deativate(
                @Schema(description = "탈퇴 사유 코드", example = "R001") ReasonCode reasonCode,
                @Schema(description = "탈퇴 사유") String reason,
                @Schema(description = DescriptionUtils.PASSWORD) @NotBlank String password,
                @Schema(description = DescriptionUtils.SECRET_KEY) @Length(min = 32) String secretKey) { }

        @Schema(name = "UserPasswordModification")
        public record PasswordModification(
                @Schema(description = DescriptionUtils.PASSWORD) @NotBlank String password,
                @Schema(description = DescriptionUtils.SECRET_KEY) @Length(min = 32) String secretKey) { }
    }

    public final static class Res {
        @Schema(name = "UserDetail")
        public record Detail(String email, String nickname, String imageUrl, boolean push, boolean nightPush) { }
    }
}
