package com.chaeking.api.config;

import com.chaeking.api.config.filter.LoginFilter;
import com.chaeking.api.config.module.SerializeModule;
import com.chaeking.api.service.UserService;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.json.JsonMapper;
import io.swagger.v3.core.jackson.ModelResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserService userService;

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new Pbkdf2PasswordEncoder();
//    }

    public static PasswordEncoder passwordEncoder = new Pbkdf2PasswordEncoder();

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        LoginFilter loginFilter = new LoginFilter(authenticationManager(), userService, jsonMapper());
//        JWTCheckFilter checkFilter = new JWTCheckFilter(authenticationManager(), userService);
        http
                .csrf().disable()
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class)
//                .addFilterAt(checkFilter, BasicAuthenticationFilter.class)
        ;
    }

    @Bean
    public ObjectMapper jsonMapper() {
        return JsonMapper.builder()
                .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
                .enable(SerializationFeature.INDENT_OUTPUT)
                .enable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY)
                .enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)
                .enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING)
                .disable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .addModule(SerializeModule.dateTimeModule)
                .addModule(SerializeModule.recordNamingStrategyPatchModule)
                .build();
    }

    @Bean
    public ModelResolver modelResolver(@Qualifier("jsonMapper") ObjectMapper jsonMapper) {
        ModelResolver m = new ModelResolver(jsonMapper);
        return m;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }

}
