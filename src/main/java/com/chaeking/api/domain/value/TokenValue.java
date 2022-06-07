package com.chaeking.api.domain.value;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

public class TokenValue {

    @Builder
    @Schema(name = "TokenVerify")
    public record Verify(String username, boolean success) {}
}
