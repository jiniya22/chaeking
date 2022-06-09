package com.chaeking.api.config.filter;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.chaeking.api.config.WebConfig;
import com.chaeking.api.domain.entity.User;
import com.chaeking.api.domain.value.TokenValue;
import com.chaeking.api.domain.value.UserValue;
import com.chaeking.api.domain.value.response.BaseResponse;
import com.chaeking.api.service.UserService;
import com.chaeking.api.util.JWTUtils;
import com.chaeking.api.util.cipher.AESCipher;
import lombok.SneakyThrows;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final UserService userService;

    public LoginFilter(AuthenticationManager authenticationManager, UserService userService) {
        super(authenticationManager);
        this.userService = userService;
        setFilterProcessesUrl("/v1/auth/login");
    }

    @SneakyThrows(IOException.class)
    @Transactional
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        UserValue.Req.Login userLogin = WebConfig.jsonMapper().readValue(request.getInputStream(), UserValue.Req.Login.class);
        String refreshToken = request.getHeader("refresh_token");
        if(Strings.isBlank(refreshToken)) {
            try {
                String pw = AESCipher.decrypt(userLogin.password(), userLogin.secretKey());
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                        userLogin.email(), pw, null
                );
                return getAuthenticationManager().authenticate(token);
            } catch(Exception e) {
                BaseResponse errorResponse = BaseResponse.of("access_token was expired");
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.getOutputStream().write(WebConfig.jsonMapper().writeValueAsBytes(errorResponse));
                return null;
            }
        } else {
            TokenValue.Verify verify = JWTUtils.verify(refreshToken);
            if(verify.success()) {
                User user = userService.loadUserByUsername(verify.username());
                return new UsernamePasswordAuthenticationToken(user, user.getAuthorities());
            } else {
                throw new TokenExpiredException("refresh_token was expired");
            }
        }
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult) throws IOException
    {
        User user = (User) authResult.getPrincipal();
        TokenValue.Token token = TokenValue.Token.of(user);
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().write(WebConfig.jsonMapper().writeValueAsBytes(token));
    }
    
}
