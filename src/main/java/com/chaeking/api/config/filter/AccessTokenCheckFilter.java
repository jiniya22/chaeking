package com.chaeking.api.config.filter;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.chaeking.api.domain.entity.User;
import com.chaeking.api.domain.value.TokenValue;
import com.chaeking.api.domain.value.response.BaseResponse;
import com.chaeking.api.service.UserService;
import com.chaeking.api.util.JWTUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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

public class AccessTokenCheckFilter extends BasicAuthenticationFilter {
    private UserService userService;
    private ObjectMapper jsonMapper;

    public AccessTokenCheckFilter(AuthenticationManager authenticationManager, UserService userService, ObjectMapper jsonMapper) {
        super(authenticationManager);
        this.userService = userService;
        this.jsonMapper = jsonMapper;
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
        String reason = null;
        try {
            TokenValue.Verify result = JWTUtils.verify(token);
            if(result.success()) {
                User user = userService.loadUserByUsername(result.username());
                UsernamePasswordAuthenticationToken userToken = new UsernamePasswordAuthenticationToken(
                        user.getUsername(), null, user.getAuthorities()
                );
                SecurityContextHolder.getContext().setAuthentication(userToken);
                response.setHeader("X-Chaeking-User-Id", user.getId().toString());
                chain.doFilter(request, response);
            } else {
                reason = "access_token 이 유효하지 않습니다.";
            }
        } catch (JWTDecodeException e) {
            reason = "access_token 의 형식이 올바르지 않습니다.";
        } catch (Exception e) {
            reason = e.getMessage();
        }
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getOutputStream().write(jsonMapper.writeValueAsBytes(BaseResponse.of(reason)));
    }
}
