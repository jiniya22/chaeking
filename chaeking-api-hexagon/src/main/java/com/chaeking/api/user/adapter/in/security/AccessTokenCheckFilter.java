package com.chaeking.api.user.adapter.in.security;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.chaeking.api.common.util.JWTUtils;
import com.chaeking.api.common.util.ResponseWriterUtil;
import com.chaeking.api.user.application.port.in.GetUserCommand;
import com.chaeking.api.user.application.port.in.GetUserQuery;
import com.chaeking.api.user.domain.TokenVerify;
import com.chaeking.api.user.domain.User;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

class AccessTokenCheckFilter extends BasicAuthenticationFilter {
    private static final String UNAUTHORIZED_AUTHORIZATION_INVALID = "access_token 이 유효하지 않습니다.";
    private static final String UNAUTHORIZED_AUTHORIZATION_FORMAT_ERROR = "access_token 의 형식이 올바르지 않습니다.";

    private final GetUserQuery getUserQuery;

    public AccessTokenCheckFilter(AuthenticationManager authenticationManager, GetUserQuery getUserQuery) {
        super(authenticationManager);
        this.getUserQuery = getUserQuery;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        String bearer = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(bearer == null || !bearer.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }
        String token = bearer.substring("Bearer ".length());
        String reason;
        try {
            TokenVerify result = JWTUtils.verify(token);
            if (result.success()) {
                User user = getUserQuery.getUser(result.uid());
//                if (result.key().equals(user.getRefreshKey())) {
                    UsernamePasswordAuthenticationToken userToken = new UsernamePasswordAuthenticationToken(
                            user.getUsername(), null, user.getAuthorities()
                    );
                    SecurityContextHolder.getContext().setAuthentication(userToken);
                    response.setHeader("X-Chaeking-User-Id", user.getId().toString());
                    chain.doFilter(request, response);
                    return;
//                }
            }
            reason = UNAUTHORIZED_AUTHORIZATION_INVALID;
        } catch (JWTDecodeException e) {
            reason =  UNAUTHORIZED_AUTHORIZATION_FORMAT_ERROR;
        } catch (Exception e) {
            reason = e.getMessage();
        }
        ResponseWriterUtil.writeBaseResponse(response, HttpServletResponse.SC_UNAUTHORIZED, reason);
    }
}
