package com.chaeking.api.common.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.chaeking.api.config.property.ChaekingConfig;
import com.chaeking.api.user.domain.TokenVerify;

import java.time.Instant;
import java.util.Optional;

public class JWTUtils {
    private static final long ACCESS_TIME = 60 * 60 * 6; // 6 hours
    private static final long REFRESH_TIME = 60 * 60 * 24 * 7; // 7 days

    public static String createAccessToken(long userId, String key) {
        return JWT.create()
                .withClaim("uid", userId)
                .withClaim("exp", Instant.now().getEpochSecond() + ACCESS_TIME)
                .withClaim("key", key)
                .sign(getAlgorithm());
    }

    public static String createRefreshToken(long userId, String key) {
        return JWT.create()
                .withClaim("uid", userId)
                .withClaim("exp", Instant.now().getEpochSecond() + REFRESH_TIME)
                .withClaim("key", key)
                .sign(getAlgorithm());
    }

    public static TokenVerify verify(String token){
        try {
            DecodedJWT verify = JWT.require(getAlgorithm()).build().verify(token);
            return TokenVerify.builder().success(true)
                    .uid(verify.getClaim("uid").asLong())
                    .key(Optional.ofNullable(verify.getClaim("key")).map(Claim::asString).orElse(null)).build();
        } catch(Exception ex){
            DecodedJWT decode = JWT.decode(token);
            return TokenVerify.builder().success(false)
                    .uid(decode.getClaim("uid").asLong()).build();
        }
    }

    public static String getKey(String token){
        try {
            DecodedJWT decode = JWT.decode(token);
            return Optional.ofNullable(decode.getClaim("key")).map(Claim::asString).orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

    public static Algorithm getAlgorithm() {
        return Algorithm.HMAC256(ChaekingConfig.getSecret());
    }
}