package com.chaeking.api.user.application.port.in;

import com.chaeking.api.user.domain.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface GetUserQuery extends UserDetailsService {

    User getUser(GetUserCommand getUserCommand);

}
