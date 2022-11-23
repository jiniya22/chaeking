package com.chaeking.api.user.application.service;

import com.chaeking.api.common.annotation.UseCase;
import com.chaeking.api.config.exception.BadRequestException;
import com.chaeking.api.user.application.port.in.GetUserCommand;
import com.chaeking.api.user.application.port.in.GetUserQuery;
import com.chaeking.api.user.application.port.out.LoadUserPort;
import com.chaeking.api.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@UseCase
class GetUserService implements GetUserQuery {

    private final LoadUserPort loadUserPort;

    @Override
    public User getUser(GetUserCommand getUserCommand) {
        if (getUserCommand.getUserId() != null) {
            return loadUserPort.loadUserById(getUserCommand.getUserId());
        } else if (!getUserCommand.getEmail().isBlank()) {
            return loadUserByUsername(getUserCommand.getEmail());
        }
        throw new BadRequestException("id 또는 email 값이 있어야 합니다."); // FIXME 문구 변경 필요
    }

    @Override
    public User loadUserByUsername(String username) {
        return loadUserPort.loadUserByUsername(username);
    }
}
