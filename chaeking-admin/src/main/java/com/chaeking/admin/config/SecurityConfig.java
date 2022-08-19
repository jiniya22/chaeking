package com.chaeking.admin.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	private final static String[] allowedUrls = {"/", "/login", "/join", "/**"};
		
	@Override
	public void configure(WebSecurity web) {
		web.ignoring().antMatchers("/static/**", "/api-docs", "/api-docs/**", "/robots.txt");
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.authorizeRequests()
			.antMatchers("/**").permitAll()
			.antMatchers("/users").hasRole("ADMIN")
			.anyRequest().authenticated()
		.and()
			.formLogin().loginPage("/login").defaultSuccessUrl("/", true)
			.usernameParameter("email").passwordParameter("password")
		.and()
			.logout().invalidateHttpSession(true).deleteCookies("JSESSIONID")
		.and().csrf()
			.ignoringAntMatchers(allowedUrls)
			.requireCsrfProtectionMatcher(new CsrfRequireMatcher())
			.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
	}
		
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	static class CsrfRequireMatcher implements RequestMatcher {
	    private static final Pattern ALLOWED_METHODS = Pattern.compile("^(GET|HEAD|TRACE|OPTIONS)$");
	    
	    @Override
	    public boolean matches(HttpServletRequest request) {
			return !ALLOWED_METHODS.matcher(request.getMethod()).matches();
		}
	}
}
