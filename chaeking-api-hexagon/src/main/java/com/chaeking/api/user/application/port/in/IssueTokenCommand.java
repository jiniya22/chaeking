package com.chaeking.api.user.application.port.in;

import com.chaeking.api.common.SelfValidating;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Getter
@AllArgsConstructor
public class IssueTokenCommand extends SelfValidating<IssueTokenCommand> {
    @Min(1)
    private final Long userId;

    @NotEmpty
    private final String refreshToken;
}
