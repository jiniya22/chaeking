package com.chaeking.api.service;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.chaeking.api.config.SecurityConfig;
import com.chaeking.api.config.exception.InvalidInputException;
import com.chaeking.api.domain.entity.User;
import com.chaeking.api.domain.value.TokenValue;
import com.chaeking.api.domain.value.UserValue;
import com.chaeking.api.domain.value.response.BaseResponse;
import com.chaeking.api.repository.UserRepository;
import com.chaeking.api.util.JWTUtils;
import com.chaeking.api.util.MessageUtils;
import com.chaeking.api.util.cipher.AESCipher;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    User select(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new InvalidInputException("일치하는 사용자가 없습니다(X-Chaeking-User-Id Error)"));
    }

    @Transactional
    public BaseResponse save(UserValue.Req.Creation req) {
        if(userRepository.existsByEmail(req.email()))
            throw new InvalidInputException("등록된 이메일 입니다.");
        User user = userRepository.save(User.of(req));
        user.initializeAuthorities();
        userRepository.save(user);
        return BaseResponse.of();
    }

    public UserValue.Res.Detail selectDetail(long userId) {
        return UserValue.Res.Detail.create(select(userId));
    }

    public TokenValue.Token login(String refreshToken, UserValue.Req.Login req) {
        if (Strings.isBlank(refreshToken)) {
            TokenValue.Verify verify = JWTUtils.verify(refreshToken);
            if(verify.success()) {
                User user = userRepository.findByEmail(verify.username()).orElseThrow(() -> new InvalidInputException("이메일이 유효하지 않습니다."));
                return TokenValue.Token.create(user);
            } else {
                throw new TokenExpiredException("refresh_token was expired");
            }
        } else {
            String pw = AESCipher.decrypt(req.password(), req.secretKey());
            User user = userRepository.findByEmail(req.email()).orElseThrow(() -> new InvalidInputException("이메일이 유효하지 않습니다."));
            if(SecurityConfig.passwordEncoder.matches(pw, user.getPassword())) {
                return TokenValue.Token.create(user);
            }
        }
        throw new InvalidInputException(MessageUtils.INVALID_USER_EMAIL_OR_PASSWORD);
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username).orElseThrow(() -> new InvalidInputException("일치하는 사용자가 없습니다"));
        user.getAuthorities().forEach(authority -> authority.getAuthority());
        return user;
    }
}