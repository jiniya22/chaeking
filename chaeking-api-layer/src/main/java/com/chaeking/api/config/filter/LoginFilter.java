package com.chaeking.api.config.filter;

import com.chaeking.api.config.SecurityConfig;
import com.chaeking.api.config.WebConfig;
import com.chaeking.api.domain.entity.User;
import com.chaeking.api.value.TokenValue;
import com.chaeking.api.value.UserValue;
import com.chaeking.api.value.response.DataResponse;
import com.chaeking.api.service.UserService;
import com.chaeking.api.util.JWTUtils;
import com.chaeking.api.util.MessageUtils;
import com.chaeking.api.util.ResponseWriterUtil;
import com.chaeking.api.util.cipher.AESCipher;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Transactional(readOnly = true)
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final UserService userService;

    public LoginFilter(AuthenticationManager authenticationManager, UserService userService) {
        super(authenticationManager);
        this.userService = userService;
        setAuthenticationFailureHandler(SecurityConfig.authenticationFailureHandler());
        setFilterProcessesUrl("/v1/auth/token");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String refreshToken = request.getHeader("X-Refresh-Token");
        if (Strings.isBlank(refreshToken)) {
            try {
                UserValue.Req.Login userLogin = WebConfig.jsonMapper().readValue(request.getInputStream(), UserValue.Req.Login.class);
                String pw = AESCipher.decrypt(userLogin.password(), userLogin.secretKey());
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userLogin.email(), pw, null);
                return getAuthenticationManager().authenticate(token);
            } catch (Exception e) {
                throw new AuthenticationServiceException(MessageUtils.INVALID_USER_EMAIL_OR_PASSWORD);
            }
        } else {
            TokenValue.Verify verify = JWTUtils.verify(refreshToken);
            if (verify.success()) {
                User user = userService.loadUserById(verify.uid());
                if (verify.key().equals(user.getRefreshKey())) {
                    return new UsernamePasswordAuthenticationToken(user, user.getAuthorities());
                }
            }
            throw new AuthenticationServiceException("refresh_token was expired");
        }
    }

    @Transactional
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        User user = (User) authResult.getPrincipal();
        TokenValue.Token token = User.createToken(user);
        user.setRefreshKey(JWTUtils.getKey(token.refreshToken()));
        userService.save(user);

        DataResponse<TokenValue.Token> body = DataResponse.of(token);
        ResponseWriterUtil.writeResponse(response, HttpServletResponse.SC_OK, body);
    }

}
