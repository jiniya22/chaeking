package com.chaeking.api.user.application.port.out;

public record UserDetail(String email, String nickname, String imageUrl, boolean push, boolean nightPush) {
}
