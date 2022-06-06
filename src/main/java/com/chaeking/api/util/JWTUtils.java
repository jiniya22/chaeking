package com.chaeking.api.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.chaeking.api.domain.entity.User;

import java.time.Instant;

public class JWTUtils {
    private static final Algorithm ALGORITHM = Algorithm.HMAC256("PUDJbD7yJvS7xNNsvXuZGRMhueBbTrYb");
    private static final long ACCESS_TIME = 60 * 20; // 1 day
    private static final long REFRESH_TIME = 60 * 60 * 24 * 7; // 7 days

    public static String createAccessToken(User user) {
        return JWT.create()
                .withSubject(user.getEmail())
                .withClaim("exp", Instant.now().getEpochSecond() + ACCESS_TIME)
                .sign(ALGORITHM);
    }

    public static String createRefreshToken(User user) {
        return JWT.create()
                .withSubject(user.getEmail())
                .withClaim("exp", Instant.now().getEpochSecond() + REFRESH_TIME)
                .sign(ALGORITHM);
    }

}
