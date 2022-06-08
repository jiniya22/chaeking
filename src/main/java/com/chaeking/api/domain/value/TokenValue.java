package com.chaeking.api.domain.value;

import com.chaeking.api.domain.entity.User;
import com.chaeking.api.util.JWTUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

public class TokenValue {

    @Builder
    @Schema(name = "TokenVerify")
    public record Verify(String username, boolean success) {}


    @Schema(name = "Token")
    public record Token(
            String accessToken,
            String refreshToken) {
        public final static Token of(User u) {
            return new Token(JWTUtils.createAccessToken(u), JWTUtils.createRefreshToken(u));
        }
    }
}
