package com.chaeking.api.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.chaeking.api.domain.entity.User;
import com.chaeking.api.domain.value.TokenValue;

import java.time.Instant;

public class JWTUtils {
    private static final Algorithm ALGORITHM = Algorithm.HMAC256("PUDJbD7yJvS7xNNsvXuZGRMhueBbTrYb");
    private static final long ACCESS_TIME = 60 * 60 * 24; // 1 day
    private static final long REFRESH_TIME = 60 * 60 * 24 * 7; // 7 days

    public static String createAccessToken(User user) {
        return JWT.create()
                .withSubject(user.getUsername())
//                .withClaim("uid", user.getId())
                .withClaim("exp", Instant.now().getEpochSecond() + ACCESS_TIME)
                .sign(ALGORITHM);
    }

    public static String createRefreshToken(User user) {
        return JWT.create()
                .withSubject(user.getUsername())
//                .withClaim("uid", user.getId())
                .withClaim("exp", Instant.now().getEpochSecond() + REFRESH_TIME)
                .sign(ALGORITHM);
    }

    public static TokenValue.Verify verify(String token){
        try {
            DecodedJWT verify = JWT.require(ALGORITHM).build().verify(token);
            return TokenValue.Verify.builder().success(true)
//                    .userId(verify.getClaim("uid").asLong())
                    .username(verify.getSubject()).build();
        } catch(Exception ex){
            DecodedJWT decode = JWT.decode(token);
            return TokenValue.Verify.builder().success(false)
//                    .userId(decode.getClaim("uid").asLong())
                    .username(decode.getSubject()).build();
        }
    }

}
