package com.chaeking.api.user.application.port.out;

public interface UpdateUserStatePort {

    void updateUserRefreshToken(long userId, String refreshToken);

}
