package com.chaeking.api.service;

import com.chaeking.api.config.SecurityConfig;
import com.chaeking.api.config.exception.InvalidInputException;
import com.chaeking.api.config.exception.ServerException;
import com.chaeking.api.domain.entity.User;
import com.chaeking.api.domain.repository.UserRepository;
import com.chaeking.api.model.MailValue;
import com.chaeking.api.util.MessageUtils;
import com.chaeking.api.util.RandomStringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;

@RequiredArgsConstructor
@Service
public class MailService {

    private final JavaMailSender javaMailSender;
    private final UserRepository userRepository;

    public void sendMail(MailValue.MailRequest request) {
        try {
            MimeMessagePreparator mimeMessage = getMimeMessage(request);
            javaMailSender.send(mimeMessage);
        } catch (MailException me) {
            throw new ServerException("이메일 전송 중 에러 발생");
        }
    }

    @Transactional
    public void sendTemporaryPassword(MailValue.MailTemporaryPasswordRequest req) {
        User user = userRepository.findByEmail(req.email()).orElseThrow(() -> new InvalidInputException(MessageUtils.NOT_FOUND_USER_EMAIL));

        try {
            String newPassword = RandomStringUtils.generateRandomString(10);
            String message = String.format("새로 발급된 비밀번호는 %s 입니다.", newPassword);
            user.setPassword(SecurityConfig.passwordEncoder.encode(newPassword));
            MimeMessagePreparator mimeMessage = getMimeMessage(
                    new MailValue.MailRequest(req.email(), "새로 발급된 책킹 비밀번호 입니다.", getContent(message)));
            javaMailSender.send(mimeMessage);
            userRepository.save(user);
        } catch (MailException me) {
            throw new ServerException("이메일 전송 중 에러 발생");
        }
    }

    private MimeMessagePreparator getMimeMessage(MailValue.MailRequest request) {
        return mimeMessage -> {
            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(request.email()));
            mimeMessage.setFrom(new InternetAddress("jini@chaeking.com"));
            mimeMessage.setSubject(request.title(), "UTF-8");
            mimeMessage.setHeader("Content-Type", "text/html; charset=utf-8");
            mimeMessage.setContent(request.content(), "text/html; charset=utf-8");
        };
    }

    private String getContent(String content) {
        return """
                <div style="padding: 30px;">
                  <div class="d__container" style="margin: 0 auto;">
                    <div style="margin: 20px 16px;">
                      <div style="font-weight: 700; letter-spacing: -.5px;">
                        CHAEKING
                      </div>
                    </div>
                    <div class="d-summary-main" style="background: #fff; margin: 10px 0;">
                      <div class="d-content" style="padding: 14px 16px; font-family: 'Noto Serif KR', serif;">
                        """
                        + content
                      + """
                      </div>
                      </div>
                    </div>
                  </div>
                </div>
                """;
    }
}
