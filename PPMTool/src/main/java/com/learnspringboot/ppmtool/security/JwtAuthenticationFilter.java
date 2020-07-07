package com.learnspringboot.ppmtool.security;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.learnspringboot.ppmtool.domain.User;
import com.learnspringboot.ppmtool.services.CustomUserDetailsService;

import static com.learnspringboot.ppmtool.security.SecurityConstants.HEADER_STRING;
import static com.learnspringboot.ppmtool.security.SecurityConstants.TOKEN_PREFIX;;


public class JwtAuthenticationFilter extends OncePerRequestFilter{

	@Autowired
	private JwtTokenProvider tokenProvider;
	
	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
			throws ServletException, IOException {

		try {
			String jwt = getJWTFromRequest(req);
			
			if(StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
				
				Long userId = tokenProvider.getUserIdFromJWT(jwt);
				
				User userDetails = customUserDetailsService.loadUserById(userId);
				
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails, null, Collections.EMPTY_LIST);
				
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
				
				SecurityContextHolder.getContext().setAuthentication(authentication);
				
			}
			
		} catch (Exception ex) {
			logger.error("Could not set user in security context " + ex);
		}
		
		chain.doFilter(req, res);
	}
	
	private String getJWTFromRequest(HttpServletRequest req) {
		
		String bearerToken = req.getHeader(HEADER_STRING);
		
		if(StringUtils.hasText(bearerToken) && bearerToken.startsWith(TOKEN_PREFIX)) {
			return bearerToken.substring(7, bearerToken.length());
		}
		
		return null;
	}

}
