package com.chaeking.api.service;

import com.chaeking.api.config.exception.InvalidInputException;
import com.chaeking.api.domain.entity.User;
import com.chaeking.api.domain.value.UserValue;
import com.chaeking.api.domain.value.response.BaseResponse;
import com.chaeking.api.repository.UserRepository;
import com.chaeking.api.util.cipher.AESCipher;
import com.chaeking.api.util.cipher.SHA256Cipher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    User select(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new InvalidInputException("일치하는 사용자가 없습니다(X-Chaeking-User-Id Error)"));
    }

    @Transactional
    public BaseResponse save(UserValue.Req.Creation req) {
        if(userRepository.existsByEmail(req.email()))
            throw new InvalidInputException("등록된 이메일 입니다.");
        userRepository.save(User.of(req));

        return BaseResponse.of();
    }

    public UserValue.Res.Detail selectDetail(long userId) {
        return UserValue.Res.Detail.of(select(userId));
    }

    public UserValue.Res.Token login(UserValue.Req.Login req) {
        String pw = AESCipher.decrypt(req.password(), req.secretKey());
        UserValue.Res.Token token = userRepository.findByEmailAndPassword(req.email(), SHA256Cipher.convertHash(pw))
                .map(UserValue.Res.Token::of)
                .orElseThrow(() -> new InvalidInputException("입력하신 회원정보가 잘못되었습니다."));

        return token;
    }
}