package com.healthassist.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private JwTService jwtService;

	@Autowired
	private JwtUserDetailService jwtUserDetailsService;

	@Autowired
	WebTokensAuthenticationEntryPoint webTokensAuthenticationEntryPoint;

	@Value("${jwt.header}")
	private String tokenHeader;

	@Value("${jwt.cookieName}")
	private String tokenCookieName;
	// provision for future cookie implementation

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable().exceptionHandling().and().exceptionHandling()
				.authenticationEntryPoint(webTokensAuthenticationEntryPoint).and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests()
				.antMatchers(HttpMethod.GET, "/").permitAll().antMatchers(HttpMethod.POST, "/api/v1/patient/login")
				.permitAll().antMatchers(HttpMethod.POST, "/api/v1/patient/signup").permitAll()
				.antMatchers(HttpMethod.POST, "/api/v1/counselor/login").permitAll()
				.antMatchers(HttpMethod.POST, "/api/v1/counselor/signup").permitAll()
				.antMatchers(HttpMethod.POST, "/api/v1/doctor/login").permitAll()
				.antMatchers(HttpMethod.POST, "/api/v1/doctor/signup").permitAll()
				.antMatchers(HttpMethod.GET, "/api/v1/patient/assessment/**").hasAuthority("ROLE_PATIENT")
				.antMatchers(HttpMethod.GET, "/api/v1/counselor/patient").hasAuthority("ROLE_COUNSELOR")
				.antMatchers("/api/v1/counselor/patient/**").hasAuthority("ROLE_COUNSELOR")
				.antMatchers("/api/v1/doctor/patient").hasAuthority("ROLE_DOCTOR");

		JWTAuthenticationFilter authenticationTokenFilter = new JWTAuthenticationFilter(jwtUserDetailsService,
				jwtService, tokenHeader, tokenCookieName);
		http.addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
		http.headers().cacheControl();
		return http.build();

	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		final DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(jwtUserDetailsService);
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return authenticationProvider;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
