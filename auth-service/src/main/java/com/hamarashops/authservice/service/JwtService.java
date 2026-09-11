package com.hamarashops.authservice.service;

import org.springframework.security.core.Authentication;

public interface JwtService {

	String generateToken(Authentication authentication);

	String generateTokenFromUsername(String username);

	String getUsernameFromJwt(String token);

	boolean validateJwtToken(String token);
}
