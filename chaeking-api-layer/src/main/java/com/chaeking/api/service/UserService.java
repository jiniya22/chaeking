package com.chaeking.api.service;

import com.chaeking.api.config.SecurityConfig;
import com.chaeking.api.config.exception.InvalidInputException;
import com.chaeking.api.domain.entity.Author;
import com.chaeking.api.domain.entity.User;
import com.chaeking.api.domain.entity.UserAuthority;
import com.chaeking.api.domain.entity.UserInactiveLog;
import com.chaeking.api.domain.repository.UserInactiveLogRepository;
import com.chaeking.api.model.BaseValue;
import com.chaeking.api.model.ChaekingProperties;
import com.chaeking.api.model.TokenValue;
import com.chaeking.api.model.UserValue;
import com.chaeking.api.domain.repository.UserRepository;
import com.chaeking.api.util.FileUtils;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserInactiveLogRepository userInactiveLogRepository;

    User select(Long userId) {
        if(userId == null)
            throw new AccessDeniedException(MessageUtils.UNAUTHORIZED_AUTHORIZATION_EMPTY);

        return userRepository.findById(userId)
                .orElseThrow(() -> new InvalidInputException("일치하는 사용자가 없습니다(X-Chaeking-User-Id Error)"));
    }

    @Transactional
    public TokenValue.Token save(UserValue.Req.Creation req) {
        if(userRepository.existsByEmail(req.email()))
            throw new InvalidInputException(MessageUtils.DUPLICATE_USER_EMAIL);
        User user = userRepository.save(User.of(req));
        user.initializeAuthorities();
        userRepository.save(user);
        TokenValue.Token token = User.createToken(user);
        user.setRefreshKey(JWTUtils.getKey(token.refreshToken()));
        userRepository.save(user);
        return token;
    }

    @Transactional
    public void save(User user) {
        userRepository.save(user);
    }

    @Transactional
    public void patch(long userId, UserValue.Req.Modification req) {
        User user = select(userId);
        if (Strings.isBlank(req.email()) && Strings.isBlank(req.nickname()) && req.push() == null && req.nightPush() == null) {
            log.info(">>>>> email, nickname, push, nightPush 모두 값이 없습니다.");
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
            if (req.push() != null) {
                user.setPush(req.push());
            }
            if (req.nightPush() != null) {
                user.setNightPush(req.nightPush());
            }
            userRepository.save(user);
        }
    }

    @Transactional
    public void patchPassword(Long userId, UserValue.Req.PasswordModification req) {
        User user = select(userId);
        user.setSecretKey(req.secretKey());
        user.setPassword(SecurityConfig.passwordEncoder.encode(AESCipher.decrypt(req.password(), req.secretKey())));
        userRepository.save(user);
    }

    @Transactional
    public String patchImageUrl(Long userId, MultipartFile image) {
        User user = select(userId);
        String fileName = FileUtils.uploadImageFile(image);
        String oldImage = user.getImageUrl();
        user.setImageUrl(Optional.ofNullable(fileName).map(m -> ChaekingProperties.getImageUrlPrefix() + m).orElse(null));
        userRepository.save(user);
        FileUtils.removeImageFile(oldImage);
        return oldImage;
    }

    public String checkNickname(Long userId, UserValue.Req.Nickname req) {
        if((userId == null && userRepository.existsByNickname(req.nickname()))
                || userRepository.existsByIdNotAndNickname(userId, req.nickname())) {
            return "중복된 닉네임 입니다";
        }
        return "";
    }

    public String checkEmail(Long userId, UserValue.Req.UserEmail req) {
        if((userId == null && userRepository.existsByEmail(req.email()))
                || userRepository.existsByIdNotAndEmail(userId, req.email())) {
            return "중복된 이메일 입니다";
        }
        return "";
    }

    public UserValue.Res.Detail selectDetail(long userId) {
        return User.createDetail(select(userId));
    }

    public List<BaseValue> selectAllUserAndAuthor(Long userId) {
        User user = select(userId);
        return user.getUserAndAuthors().stream().map(userAndAuthor -> {
            userAndAuthor.getAuthor().getName();
            return Author.createSimple(userAndAuthor.getAuthor());
        }).collect(Collectors.toList());
    }

    @Transactional
    public void revoke(Long userId) {
        User user = select(userId);
        user.setRefreshKey(null);
        userRepository.save(user);
    }

    @Transactional
    public void deativate(Long userId, UserValue.Req.Deativate req) {
        User user = select(userId);
        if(!SecurityConfig.passwordEncoder.matches(AESCipher.decrypt(req.password(), req.secretKey()), user.getPassword())) {
            throw new InvalidInputException(MessageUtils.INVALID_PASSWORD);
        }

        user.deactivate();
        userRepository.save(user);
        UserInactiveLog userInactiveLog = UserInactiveLog.builder().userId(userId).reasonCode(req.reasonCode())
                        .reason(req.reason()).build();
        userInactiveLogRepository.save(userInactiveLog);
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username).orElseThrow(() -> new InvalidInputException("일치하는 사용자가 없습니다"));
        user.getAuthorities().forEach(UserAuthority::getAuthority);
        return user;
    }

    public User loadUserById(Long id) throws UsernameNotFoundException {
        User user = userRepository.findById(id).orElseThrow(() -> new InvalidInputException("일치하는 사용자가 없습니다"));
        user.getAuthorities().forEach(UserAuthority::getAuthority);
        return user;
    }
}