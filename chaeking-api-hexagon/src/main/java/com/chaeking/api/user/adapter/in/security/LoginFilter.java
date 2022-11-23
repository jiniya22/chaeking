package com.chaeking.api.user.adapter.in.security;

import com.chaeking.api.common.DataResponse;
import com.chaeking.api.common.util.AESCipher;
import com.chaeking.api.common.util.JWTUtils;
import com.chaeking.api.common.util.ResponseWriterUtil;
import com.chaeking.api.config.WebConfig;
import com.chaeking.api.user.application.port.in.GetUserCommand;
import com.chaeking.api.user.application.port.in.GetUserQuery;
import com.chaeking.api.user.application.port.in.IssueTokenCommand;
import com.chaeking.api.user.application.port.in.IssueTokenUseCase;
import com.chaeking.api.user.application.port.out.UserLogin;
import com.chaeking.api.user.domain.Token;
import com.chaeking.api.user.domain.TokenVerify;
import com.chaeking.api.user.domain.User;
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

@Transactional(readOnly = true)
class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final GetUserQuery getUserQuery;
    private final IssueTokenUseCase issueTokenUseCase;

    public LoginFilter(AuthenticationManager authenticationManager, GetUserQuery getUserQuery,
                       IssueTokenUseCase issueTokenUseCase) {
        super(authenticationManager);
        this.getUserQuery = getUserQuery;
        this.issueTokenUseCase = issueTokenUseCase;
        setAuthenticationFailureHandler(SecurityConfig.authenticationFailureHandler());
        setFilterProcessesUrl("/v1/auth/token");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String refreshToken = request.getHeader("X-Refresh-Token");
        if (Strings.isBlank(refreshToken)) {
            try {
                UserLogin userLogin = WebConfig.jsonMapper().readValue(request.getInputStream(), UserLogin.class);
                String pw = AESCipher.decrypt(userLogin.password(), userLogin.secretKey());
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userLogin.email(), pw, null);
                return getAuthenticationManager().authenticate(token);
            } catch (Exception e) {
                throw new AuthenticationServiceException("이메일 또는 비밀번호를 잘못 입력했습니다.");
            }
        } else {
            TokenVerify verify = JWTUtils.verify(refreshToken);
            if (verify.success()) {
                User user = getUserQuery.getUser(GetUserCommand.builder().userId(verify.uid()).build());
                if (verify.key().equals(user.getRefreshKey())) {
                    return new UsernamePasswordAuthenticationToken(user, user.getAuthorities());
                }
            }
            throw new AuthenticationServiceException("refresh_token was expired");
        }
    }

    @Transactional
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) {
        User user = (User) authResult.getPrincipal();
        Token token = user.createToken();
        user.setRefreshKey(JWTUtils.getKey(token.refreshToken()));
        issueTokenUseCase.issueToken(new IssueTokenCommand(user.getId(), token.refreshToken()));

        DataResponse<Token> body = DataResponse.create(token);
        ResponseWriterUtil.writeResponse(response, HttpServletResponse.SC_OK, body);
    }

}
