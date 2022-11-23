package com.chaeking.api.user.adapter.out.persistence;

import com.chaeking.api.common.annotation.PersistenceAdapter;
import com.chaeking.api.config.exception.NotFoundException;
import com.chaeking.api.user.application.port.in.IssueTokenCommand;
import com.chaeking.api.user.application.port.out.LoadUserPort;
import com.chaeking.api.user.application.port.out.UpdateUserStatePort;
import com.chaeking.api.user.domain.User;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
class UserPersistenceAdapter implements LoadUserPort, UpdateUserStatePort {

    private final UserRepository userRepository;
    public static final String MESSAGE_404 = "존재하지 않는 사용자입니다.";

    @Override
    public User loadUserById(long id) {
        return userRepository.findById(id)
                .map(UserEntity::mapToUser)
                .orElseThrow(() -> new NotFoundException(MESSAGE_404));
    }

    @Override
    public User loadUserByUsername(String username) {
        return userRepository.findByEmail(username)
                .map(UserEntity::mapToUser)
                .orElseThrow(() -> new NotFoundException(MESSAGE_404));
    }

    @Override
    public void updateUserRefreshToken(long userId, String refreshToken) {
        userRepository.findById(userId).ifPresent(user -> {
            user.setRefreshKey(refreshToken);
        });
    }
}
