package com.chaeking.api.user.application.port.out;

import com.chaeking.api.user.domain.User;

public interface LoadUserPort {

    User loadUserById(long id);

    // username -> email
    User loadUserByUsername(String username);

}
