package com.chaeking.api.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.chaeking.api.config.vault.ChaekingConfig;
import com.chaeking.api.domain.entity.User;
import com.chaeking.api.model.TokenValue;
import com.chaeking.api.util.cipher.AESCipher;

import java.time.Instant;
import java.util.Optional;

public class JWTUtils {
    private static final long ACCESS_TIME = 60 * 60 * 6; // 6 hours
    private static final long REFRESH_TIME = 60 * 60 * 24 * 7; // 7 days

    public static String createAccessToken(long uid, String username, String key, String scope) {
        return JWT.create()
                .withClaim("uid", uid)
                .withClaim("username", AESCipher.encrypt(username))
                .withClaim("exp", Instant.now().getEpochSecond() + ACCESS_TIME)
                .withClaim("key", key)
                .withClaim("scope", scope)
                .sign(getAlgorithm());
    }

    public static String createRefreshToken(long uid, String key) {
        return JWT.create()
                .withClaim("uid", uid)
                .withClaim("exp", Instant.now().getEpochSecond() + REFRESH_TIME)
                .withClaim("key", key)
                .sign(getAlgorithm());
    }

    public static TokenValue.Verify verifyAccessToken(String token){
        try {
            DecodedJWT verify = JWT.require(getAlgorithm()).build().verify(token);
            return TokenValue.Verify.builder().success(true)
                    .uid(verify.getClaim("uid").asLong())
                    .username(AESCipher.decrypt(verify.getClaim("username").asString()))
                    .scope(verify.getClaim("scope").asString())
                    .key(Optional.ofNullable(verify.getClaim("key")).map(Claim::asString).orElse(null)).build();
        } catch(Exception ex){
            DecodedJWT decode = JWT.decode(token);
            return TokenValue.Verify.builder().success(false)
                    .uid(decode.getClaim("uid").asLong()).build();
        }
    }

    public static TokenValue.Verify verifyRefreshToken(String token){
        try {
            DecodedJWT verify = JWT.require(getAlgorithm()).build().verify(token);
            return TokenValue.Verify.builder().success(true)
                    .uid(verify.getClaim("uid").asLong())
                    .key(Optional.ofNullable(verify.getClaim("key")).map(Claim::asString).orElse(null)).build();
        } catch(Exception ex){
            DecodedJWT decode = JWT.decode(token);
            return TokenValue.Verify.builder().success(false)
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
