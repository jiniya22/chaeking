package com.chaeking.api.service;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.chaeking.api.config.SecurityConfig;
import com.chaeking.api.config.exception.InvalidInputException;
import com.chaeking.api.domain.entity.User;
import com.chaeking.api.domain.entity.UserAuthority;
import com.chaeking.api.domain.value.TokenValue;
import com.chaeking.api.domain.value.UserValue;
import com.chaeking.api.domain.value.response.BaseResponse;
import com.chaeking.api.repository.UserRepository;
import com.chaeking.api.util.JWTUtils;
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
    public void patch(long userId, UserValue.Req.Modification req) {
        User user = select(userId);
        if (Strings.isBlank(req.email()) && Strings.isBlank(req.nickname())) {
            log.info(">>>>> email, nickname 모두 값이 없습니다.");
        } else {
            if (Strings.isNotBlank(req.email())) {
                if (!req.email().equals(user.getEmail()) && userRepository.existsByEmail(req.email())) {
                    throw new InvalidInputException(MessageUtils.DUPLICATE_USER_EMAIL);
                }
                user.setEmail(req.email());
            }
            if (Strings.isNotBlank(req.nickname())) {
                user.setNickname(req.nickname());
            }
        }
        userRepository.save(user);
    }

    public UserValue.Res.Detail selectDetail(long userId) {
        return User.createDetail(select(userId));
    }

    public TokenValue.Token login(String refreshToken, UserValue.Req.Login req) {
        if (Strings.isBlank(refreshToken)) {
            TokenValue.Verify verify = JWTUtils.verify(refreshToken);
            if(verify.success()) {
                User user = userRepository.findByEmail(verify.username()).orElseThrow(() -> new InvalidInputException("이메일이 유효하지 않습니다."));
                return User.createToken(user);
            } else {
                throw new TokenExpiredException("refresh_token was expired");
            }
        } else {
            String pw = AESCipher.decrypt(req.password(), req.secretKey());
            User user = userRepository.findByEmail(req.email()).orElseThrow(() -> new InvalidInputException("이메일이 유효하지 않습니다."));
            if(SecurityConfig.passwordEncoder.matches(pw, user.getPassword())) {
                return User.createToken(user);
            }
        }
        throw new InvalidInputException(MessageUtils.INVALID_USER_EMAIL_OR_PASSWORD);
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username).orElseThrow(() -> new InvalidInputException("일치하는 사용자가 없습니다"));
        user.getAuthorities().forEach(UserAuthority::getAuthority);
        return user;
    }
}