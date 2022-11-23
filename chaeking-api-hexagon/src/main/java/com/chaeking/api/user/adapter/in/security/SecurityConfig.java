package com.chaeking.api.user.adapter.in.security;

import com.chaeking.api.common.util.ResponseWriterUtil;
import com.chaeking.api.user.application.port.in.GetUserQuery;
import com.chaeking.api.user.application.port.in.IssueTokenUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
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

import javax.servlet.http.HttpServletResponse;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class SecurityConfig {
    public static PasswordEncoder passwordEncoder = new Pbkdf2PasswordEncoder();

    private final GetUserQuery getUserQuery;
    private final IssueTokenUseCase issueTokenUseCase;

    public SecurityConfig(GetUserQuery getUserQuery, IssueTokenUseCase issueTokenUseCase) {
        this.getUserQuery = getUserQuery;
        this.issueTokenUseCase = issueTokenUseCase;
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
                .apply(CustomDslConfig.customDsl(getUserQuery, issueTokenUseCase))
        ;
        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(getUserQuery);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    static class CustomDslConfig extends AbstractHttpConfigurer<CustomDslConfig, HttpSecurity> {
        private static CustomDslConfig instance;
        private final GetUserQuery getUserQuery;
        private final IssueTokenUseCase issueTokenUseCase;

        private CustomDslConfig(GetUserQuery getUserQuery, IssueTokenUseCase issueTokenUseCase) {
            this.getUserQuery = getUserQuery;
            this.issueTokenUseCase = issueTokenUseCase;
        }
        @Override
        public void configure(HttpSecurity http) {
            AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
            http.addFilterAfter(new ApiOriginFilter(), HeaderWriterFilter.class)
                    .addFilterAt(new LoginFilter(authenticationManager, getUserQuery, issueTokenUseCase), UsernamePasswordAuthenticationFilter.class)
                    .addFilterAt(new AccessTokenCheckFilter(authenticationManager, getUserQuery), BasicAuthenticationFilter.class);
        }

        public static CustomDslConfig customDsl(GetUserQuery getUserQuery, IssueTokenUseCase issueTokenUseCase) {
            if(instance == null)
                instance = new CustomDslConfig(getUserQuery, issueTokenUseCase);
            return instance;
        }
    }

    public static AuthenticationFailureHandler authenticationFailureHandler() {
        return (request, response, ex) ->
                ResponseWriterUtil.writeBaseResponse(response, HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
    }

}
