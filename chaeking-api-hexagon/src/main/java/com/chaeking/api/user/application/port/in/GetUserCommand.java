package com.chaeking.api.user.application.port.in;

import com.chaeking.api.common.SelfValidating;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;

@Getter
public class GetUserCommand extends SelfValidating<GetUserCommand> {
    @Min(1)
    private final Long userId;

    @Email
    private final String email;

    @Builder
    public GetUserCommand(Long userId, String email) {
        this.userId = userId;
        this.email = email;
    }
}
