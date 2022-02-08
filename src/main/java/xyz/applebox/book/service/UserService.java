package xyz.applebox.book.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.applebox.book.config.exception.InvalidInputException;
import xyz.applebox.book.domain.dto.data.UserDto;
import xyz.applebox.book.domain.dto.response.BaseResponse;
import xyz.applebox.book.domain.entity.User;
import xyz.applebox.book.repository.UserRepository;

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
                .birthDate(req.getBirthDate())
                .sex(req.getSex())
                .password(req.getPassword()).build();
        userRepository.save(user);

        return new BaseResponse();
    }
}
