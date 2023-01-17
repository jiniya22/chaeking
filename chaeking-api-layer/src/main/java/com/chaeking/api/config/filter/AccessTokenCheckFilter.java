package com.chaeking.api.config.filter;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.chaeking.api.domain.entity.User;
import com.chaeking.api.model.TokenValue;
import com.chaeking.api.service.UserService;
import com.chaeking.api.util.JWTUtils;
import com.chaeking.api.util.MessageUtils;
import com.chaeking.api.util.ResponseWriterUtil;
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

public class AccessTokenCheckFilter extends BasicAuthenticationFilter {
    private final UserService userService;

    public AccessTokenCheckFilter(AuthenticationManager authenticationManager, UserService userService) {
        super(authenticationManager);
        this.userService = userService;
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
            TokenValue.Verify result = JWTUtils.verify(token);
            if (result.success()) {
                User user = userService.loadUserById(result.uid());
                if (result.key().equals(user.getRefreshKey())) {
                    UsernamePasswordAuthenticationToken userToken = new UsernamePasswordAuthenticationToken(
                            user.getUsername(), null, user.getAuthorities()
                    );
                    SecurityContextHolder.getContext().setAuthentication(userToken);
                    response.setHeader("X-Chaeking-User-Id", user.getId().toString());
                    chain.doFilter(request, response);
                    return;
                }
            }
            reason = MessageUtils.UNAUTHORIZED_AUTHORIZATION_INVALID;
        } catch (JWTDecodeException e) {
            reason =  MessageUtils.UNAUTHORIZED_AUTHORIZATION_FORMAT_ERROR;
        } catch (Exception e) {
            reason = e.getMessage();
        }
        ResponseWriterUtil.writeBaseResponse(response, HttpServletResponse.SC_UNAUTHORIZED, reason);
    }
}
