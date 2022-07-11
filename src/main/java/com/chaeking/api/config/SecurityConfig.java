package com.chaeking.api.config;

import com.chaeking.api.config.filter.AccessTokenCheckFilter;
import com.chaeking.api.config.filter.ApiOriginFilter;
import com.chaeking.api.config.filter.LoginFilter;
import com.chaeking.api.service.UserService;
import com.chaeking.api.util.ResponseWriterUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.header.HeaderWriterFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.regex.Pattern;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class SecurityConfig {
    public static PasswordEncoder passwordEncoder = new Pbkdf2PasswordEncoder();
    private final UserService userService;
    public SecurityConfig(UserService userService) {
        this.userService = userService;
    }

    @Order(1)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .cors().and()
                .apply(CustomDslConfig.customDsl(userService))
        ;
        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    static class CustomDslConfig extends AbstractHttpConfigurer<CustomDslConfig, HttpSecurity> {
        private static CustomDslConfig instance;
        private final UserService userService;
        private CustomDslConfig(UserService userService) {
            this.userService = userService;
        }
        @Override
        public void configure(HttpSecurity http) {
            AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
            http.addFilterAfter(new ApiOriginFilter(), HeaderWriterFilter.class)
                    .addFilterAt(new LoginFilter(authenticationManager, userService), UsernamePasswordAuthenticationFilter.class)
                    .addFilterAt(new AccessTokenCheckFilter(authenticationManager, userService), BasicAuthenticationFilter.class);
        }

        public static CustomDslConfig customDsl(UserService userService) {
            if(instance == null)
                instance = new CustomDslConfig(userService);
            return instance;
        }
    }

    public static AuthenticationFailureHandler authenticationFailureHandler() {
        return (request, response, ex) -> {
            ResponseWriterUtil.writeBaseResponse(response, HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
        };
    }

}
