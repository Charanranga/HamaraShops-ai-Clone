package com.hamarashops.authservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.hamarashops.authservice.security.JwtTokenProvider;
import com.hamarashops.authservice.service.JwtService;

@Service
public class JwtServiceImpl implements JwtService {

	@Autowired
	private JwtTokenProvider tokenProvider;

	@Override
	public String generateToken(Authentication authentication) {
		return tokenProvider.generateToken(authentication);
	}

	@Override
	public String generateTokenFromUsername(String username) {
		return tokenProvider.generateTokenFromUsername(username);
	}

	@Override
	public String getUsernameFromJwt(String token) {
		return tokenProvider.getUsernameFromJWT(token);
	}

	@Override
	public boolean validateJwtToken(String token) {
		return tokenProvider.validateToken(token);
	}
}
