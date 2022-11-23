package com.chaeking.api.user.domain;

import lombok.Builder;

@Builder
public record TokenVerify(Long uid, String key, boolean success) {
}
