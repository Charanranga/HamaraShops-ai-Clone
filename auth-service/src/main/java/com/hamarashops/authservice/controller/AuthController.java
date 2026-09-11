package com.hamarashops.authservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hamarashops.authservice.dto.LoginRequest;
import com.hamarashops.authservice.dto.LoginResponse;
import com.hamarashops.authservice.dto.RefreshTokenRequest;
import com.hamarashops.authservice.dto.RefreshTokenResponse;
import com.hamarashops.authservice.dto.RegisterRequest;
import com.hamarashops.authservice.dto.RegisterResponse;
import com.hamarashops.authservice.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication API", description = "REST APIs for User Registration, Login, Token Refresh, and Logout")
public class AuthController {

	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

	@Autowired
	private AuthService authService;

	@PostMapping("/register")
	@Operation(summary = "Register User", description = "Register a new user account with BCrypt password hashing")
	public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
		logger.info("REST POST /auth/register invoked for username: {}", request.getUsername());
		RegisterResponse response = authService.register(request);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@PostMapping("/login")
	@Operation(summary = "Login User", description = "Authenticate credentials and issue JWT Access Token and Refresh Token")
	public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
		logger.info("REST POST /auth/login invoked for user: {}", request.getUsername());
		LoginResponse response = authService.login(request);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/refresh")
	@Operation(summary = "Refresh Token", description = "Generate a new JWT Access Token using a valid Refresh Token")
	public ResponseEntity<RefreshTokenResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
		logger.info("REST POST /auth/refresh invoked");
		RefreshTokenResponse response = authService.refreshToken(request);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/logout")
	@Operation(summary = "Logout User", description = "Revoke and invalidate user Refresh Token")
	public ResponseEntity<String> logout(@Valid @RequestBody RefreshTokenRequest request) {
		logger.info("REST POST /auth/logout invoked");
		authService.logout(request.getRefreshToken());
		return new ResponseEntity<>("User logged out successfully", HttpStatus.OK);
	}
}
