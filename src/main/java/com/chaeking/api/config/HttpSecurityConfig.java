package com.chaeking.api.config;

import com.chaeking.api.config.filter.AccessTokenCheckFilter;
import com.chaeking.api.config.filter.ApiOriginFilter;
import com.chaeking.api.config.filter.LoginFilter;
import com.chaeking.api.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.header.HeaderWriterFilter;

public class HttpSecurityConfig extends AbstractHttpConfigurer<HttpSecurityConfig, HttpSecurity> {
    private static HttpSecurityConfig instance;
    private final UserService userService;
    private final ObjectMapper jsonMapper;
    private HttpSecurityConfig(UserService userService, ObjectMapper jsonMapper) {
        this.userService = userService;
        this.jsonMapper = jsonMapper;
    }
    @Override
    public void configure(HttpSecurity http) {
        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
        http.addFilterAfter(new ApiOriginFilter(), HeaderWriterFilter.class)
                .addFilterAt(new LoginFilter(authenticationManager, userService, jsonMapper), UsernamePasswordAuthenticationFilter.class)
                .addFilterAt(new AccessTokenCheckFilter(authenticationManager, userService, jsonMapper), BasicAuthenticationFilter.class);
    }

    public static HttpSecurityConfig customDsl(UserService userService, ObjectMapper jsonMapper) {
        if(instance == null)
            instance = new HttpSecurityConfig(userService, jsonMapper);
        return instance;
    }
}
