package com.chaeking.api.user.application.port.in;

import com.chaeking.api.user.application.port.out.UserDetail;
import com.chaeking.api.user.domain.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface GetUserQuery extends UserDetailsService {

    User getUser(Long userId);

    UserDetail getUserDetail(long userId);

}
