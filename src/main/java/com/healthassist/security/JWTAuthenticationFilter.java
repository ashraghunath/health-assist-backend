package com.healthassist.security;
 /**
  * Use this filter for every request
  */
import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import lombok.RequiredArgsConstructor;


public class JWTAuthenticationFilter extends OncePerRequestFilter {
	
	private final JwTService jwtService;
	
	@Autowired
    private JwtUserDetailService jwtUserDetailsService;
	 
	private String tokenHeader;
	  
	private String tokenCookieName;
	  
	public JWTAuthenticationFilter(JwtUserDetailService jwtUserDetailsService, JwTService jwtService,
			String tokenHeader, String tokenCookieName) {
		this.jwtUserDetailsService = jwtUserDetailsService;
	    this.jwtService = jwtService;
	    this.tokenHeader = tokenHeader;
	    this.tokenCookieName = tokenCookieName;
	}

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
			throws ServletException, IOException {
		
		final String authHeader =request.getHeader("Authorization");
		final String jwt;
		final String userEmail;
		 //check if token exist
		if(authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}
		
		jwt = authHeader.substring(7);
		System.out.println("here in the do filter");
		userEmail = jwtService.extractUsernameFromToken(jwt);
		if(userEmail!=null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(userEmail);
			if (jwtService.validateToken(authHeader, userDetails)) {
		        UsernamePasswordAuthenticationToken authentication =
		            new UsernamePasswordAuthenticationToken(userDetails, null,
		                userDetails.getAuthorities());
		        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		        System.out.println("authorizated user '{}', setting security context"+ userEmail);
		        SecurityContextHolder.getContext().setAuthentication(authentication);
		      }
		}
		
	}



}
