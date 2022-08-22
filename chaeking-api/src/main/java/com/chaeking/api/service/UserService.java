package com.chaeking.api.service;

import com.chaeking.api.config.SecurityConfig;
import com.chaeking.api.config.exception.InvalidInputException;
import com.chaeking.api.domain.entity.Author;
import com.chaeking.api.domain.entity.User;
import com.chaeking.api.domain.entity.UserAuthority;
import com.chaeking.api.domain.value.BaseValue;
import com.chaeking.api.domain.value.ChaekingProperties;
import com.chaeking.api.domain.value.TokenValue;
import com.chaeking.api.domain.value.UserValue;
import com.chaeking.api.repository.UserRepository;
import com.chaeking.api.util.FileUtils;
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

    User select(Long userId) {
        if(userId == null)
            throw new AccessDeniedException(MessageUtils.UNAUTHORIZED_AUTHORIZATION_EMPTY);

        return userRepository.findById(userId)
                .orElseThrow(() -> new InvalidInputException("일치하는 사용자가 없습니다(X-Chaeking-User-Id Error)"));
    }

    @Transactional
    public void save(UserValue.Req.Creation req) {
        if(userRepository.existsByEmail(req.email()))
            throw new InvalidInputException(MessageUtils.DUPLICATE_USER_EMAIL);
        User user = userRepository.save(User.of(req));
        user.initializeAuthorities();
        userRepository.save(user);
    }

    @Transactional
    public TokenValue.Token patch(long userId, UserValue.Req.Modification req) {
        User user = select(userId);
        if (Strings.isBlank(req.email()) && Strings.isBlank(req.nickname()) && req.push() == null && req.nightPush() == null) {
            log.info(">>>>> email, nickname, push, nightPush 모두 값이 없습니다.");
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
            if (req.push() != null) {
                user.setPush(req.push());
            }
            if (req.nightPush() != null) {
                user.setNightPush(req.nightPush());
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