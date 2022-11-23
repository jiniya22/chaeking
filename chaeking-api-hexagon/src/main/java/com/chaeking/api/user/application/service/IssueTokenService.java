package com.chaeking.api.user.application.service;

import com.chaeking.api.common.annotation.UseCase;
import com.chaeking.api.user.application.port.in.IssueTokenCommand;
import com.chaeking.api.user.application.port.in.IssueTokenUseCase;
import com.chaeking.api.user.application.port.out.UpdateUserStatePort;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@UseCase
class IssueTokenService implements IssueTokenUseCase {

    private final UpdateUserStatePort updateUserStatePort;

    @Transactional
    @Override
    public void issueToken(IssueTokenCommand command) {
        updateUserStatePort.updateUserRefreshToken(command.getUserId(), command.getRefreshToken());
    }
}
