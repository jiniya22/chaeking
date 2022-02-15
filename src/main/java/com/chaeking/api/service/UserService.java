package com.chaeking.api.service;

import com.chaeking.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.chaeking.api.config.exception.InvalidInputException;
import com.chaeking.api.domain.dto.data.UserDto;
import com.chaeking.api.domain.dto.response.BaseResponse;
import com.chaeking.api.domain.entity.User;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public BaseResponse save(UserDto req) {
        if(userRepository.existsByEmail(req.getEmail()))
            throw new InvalidInputException("등록된 이메일 입니다.");

        User user = User.builder()
                .email(req.getEmail())
                .name(req.getName())
                .birthDate(req.getBirth_date())
                .sex(req.getSex())
                .password(req.getPassword()).build();
        userRepository.save(user);

        return new BaseResponse();
    }
}