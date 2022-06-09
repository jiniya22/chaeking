package com.chaeking.api.config;

import com.chaeking.api.config.filter.AccessTokenCheckFilter;
import com.chaeking.api.config.filter.LoginFilter;
import com.chaeking.api.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    public static PasswordEncoder passwordEncoder = new Pbkdf2PasswordEncoder();
    private final UserService userService;
    private final ObjectMapper jsonMapper;
    public SecurityConfig(UserService userService, @Qualifier("jsonMapper") ObjectMapper jsonMapper) {
        this.userService = userService;
        this.jsonMapper = jsonMapper;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        LoginFilter loginFilter = new LoginFilter(authenticationManager(), userService, jsonMapper);
        AccessTokenCheckFilter accessTokenCheckFilter = new AccessTokenCheckFilter(authenticationManager(), userService, jsonMapper);
        http
                .csrf().disable()
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAt(accessTokenCheckFilter, BasicAuthenticationFilter.class)
        ;
    }



    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userService)
                .passwordEncoder(passwordEncoder);
    }

}
