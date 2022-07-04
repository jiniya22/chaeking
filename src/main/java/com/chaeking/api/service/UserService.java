package com.chaeking.api.service;

import com.chaeking.api.config.SecurityConfig;
import com.chaeking.api.config.exception.InvalidInputException;
import com.chaeking.api.domain.entity.User;
import com.chaeking.api.domain.entity.UserAuthority;
import com.chaeking.api.domain.value.TokenValue;
import com.chaeking.api.domain.value.UserValue;
import com.chaeking.api.domain.value.response.BaseResponse;
import com.chaeking.api.repository.UserRepository;
import com.chaeking.api.util.MessageUtils;
import com.chaeking.api.util.cipher.AESCipher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    User select(Long userId) {
        if(userId == null)
            throw new AccessDeniedException(MessageUtils.UNAUTHORIZED_AUTHORIZATION_EMPTY);

        return userRepository.findById(userId)
                .orElseThrow(() -> new InvalidInputException("일치하는 사용자가 없습니다(X-Chaeking-User-Id Error)"));
    }

    @Transactional
    public BaseResponse save(UserValue.Req.Creation req) {
        if(userRepository.existsByEmail(req.email()))
            throw new InvalidInputException(MessageUtils.DUPLICATE_USER_EMAIL);
        User user = userRepository.save(User.of(req));
        user.initializeAuthorities();
        userRepository.save(user);
        return BaseResponse.of();
    }

    @Transactional
    public TokenValue.Token patch(long userId, UserValue.Req.Modification req) {
        User user = select(userId);
        if (Strings.isBlank(req.email()) && Strings.isBlank(req.nickname())) {
            log.info(">>>>> email, nickname 모두 값이 없습니다.");
        } else {
            if (Strings.isNotBlank(req.email())) {
                if (!req.email().equals(user.getEmail()) && userRepository.existsByEmail(req.email())) {
                    throw new InvalidInputException(MessageUtils.DUPLICATE_USER_EMAIL);
                }
                user.setEmail(req.email());
                userRepository.save(user);
                return User.createToken(user);
            }
            if (Strings.isNotBlank(req.nickname())) {
                user.setNickname(req.nickname());
            }
        }
        return null;
    }

    @Transactional
    public void patchPassword(Long userId, UserValue.Req.PasswordModification req) {
        User user = select(userId);
        user.setSecretKey(req.secretKey());
        user.setPassword(SecurityConfig.passwordEncoder.encode(AESCipher.decrypt(req.password(), req.secretKey())));
        userRepository.save(user);
    }

    public UserValue.Res.Detail selectDetail(long userId) {
        return User.createDetail(select(userId));
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username).orElseThrow(() -> new InvalidInputException("일치하는 사용자가 없습니다"));
        user.getAuthorities().forEach(UserAuthority::getAuthority);
        return user;
    }

}