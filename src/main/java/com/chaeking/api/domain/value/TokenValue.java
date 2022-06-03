package com.chaeking.api.domain.value;

public record TokenValue(
        String accessToken,
        String refreshToken) {
}
