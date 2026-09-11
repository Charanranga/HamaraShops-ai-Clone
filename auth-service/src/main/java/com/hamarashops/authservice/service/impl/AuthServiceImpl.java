package com.hamarashops.authservice.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hamarashops.authservice.dto.LoginRequest;
import com.hamarashops.authservice.dto.LoginResponse;
import com.hamarashops.authservice.dto.RefreshTokenRequest;
import com.hamarashops.authservice.dto.RefreshTokenResponse;
import com.hamarashops.authservice.dto.RegisterRequest;
import com.hamarashops.authservice.dto.RegisterResponse;
import com.hamarashops.authservice.exception.DuplicateUserException;
import com.hamarashops.authservice.exception.ResourceNotFoundException;
import com.hamarashops.authservice.model.RefreshToken;
import com.hamarashops.authservice.model.Role;
import com.hamarashops.authservice.model.User;
import com.hamarashops.authservice.repo.RoleRepo;
import com.hamarashops.authservice.repo.UserRepo;
import com.hamarashops.authservice.service.AuthService;
import com.hamarashops.authservice.service.JwtService;
import com.hamarashops.authservice.service.RefreshTokenService;
import com.hamarashops.authservice.client.AuditServiceClient;
import com.hamarashops.authservice.client.UserServiceClient;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

	private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private RoleRepo roleRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private RefreshTokenService refreshTokenService;

	@Autowired
	private AuditServiceClient auditServiceClient;

	@Autowired
	private UserServiceClient userServiceClient;

	@Override
	public RegisterResponse register(RegisterRequest request) {
		logger.info("Processing user registration for username: {}, email: {}", request.getUsername(), request.getEmail());

		if (userRepo.existsByUsername(request.getUsername())) {
			throw new DuplicateUserException("Username '" + request.getUsername() + "' is already taken!");
		}

		if (userRepo.existsByEmail(request.getEmail())) {
			throw new DuplicateUserException("Email '" + request.getEmail() + "' is already registered!");
		}

		Role defaultRole = roleRepo.findByRoleName("ROLE_USER")
				.orElseGet(() -> roleRepo.save(new Role("ROLE_USER")));

		User user = new User();
		user.setFullName(request.getFullName());
		user.setUsername(request.getUsername());
		user.setEmail(request.getEmail());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setPhone(request.getPhone());
		user.setEnabled(true);
		user.setRole(defaultRole);

		User savedUser = userRepo.save(user);
		logger.info("Successfully registered user ID: {}, username: {}", savedUser.getId(), savedUser.getUsername());

		// Inter-service call: Create user profile record in userdb via User Service
		userServiceClient.createUserProfile(savedUser.getFullName(), savedUser.getEmail(), savedUser.getPhone(), "");

		RegisterResponse response = new RegisterResponse();
		response.setId(savedUser.getId());
		response.setFullName(savedUser.getFullName());
		response.setUsername(savedUser.getUsername());
		response.setEmail(savedUser.getEmail());
		response.setPhone(savedUser.getPhone());
		response.setRole(savedUser.getRole().getRoleName());
		response.setMessage("User registered successfully");
		response.setCreatedAt(savedUser.getCreatedAt());

		auditServiceClient.logAudit(
				savedUser.getId(),
				"Register",
				"User",
				String.valueOf(savedUser.getId()),
				"User registered successfully: " + savedUser.getUsername(),
				"SUCCESS"
		);

		return response;
	}

	@Override
	public LoginResponse login(LoginRequest request) {
		logger.info("Processing login for user: {}", request.getUsername());

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		String jwt = jwtService.generateToken(authentication);
		RefreshToken refreshToken = refreshTokenService.createRefreshToken(authentication.getName());

		User user = userRepo.findByUsernameOrEmail(authentication.getName(), authentication.getName())
				.orElseThrow(() -> new ResourceNotFoundException("User not found: " + authentication.getName()));

		logger.info("User {} successfully authenticated", user.getUsername());

		LoginResponse response = new LoginResponse();
		response.setAccessToken(jwt);
		response.setRefreshToken(refreshToken.getToken());
		response.setTokenType("Bearer");
		response.setId(user.getId());
		response.setUsername(user.getUsername());
		response.setEmail(user.getEmail());
		response.setRole(user.getRole() != null ? user.getRole().getRoleName() : "ROLE_USER");

		auditServiceClient.logAudit(
				user.getId(),
				"Login",
				"User",
				String.valueOf(user.getId()),
				"User logged in successfully: " + user.getUsername(),
				"SUCCESS"
		);

		return response;
	}

	@Override
	public RefreshTokenResponse refreshToken(RefreshTokenRequest request) {
		String requestRefreshToken = request.getRefreshToken();
		logger.info("Processing token refresh request");

		return refreshTokenService.findByToken(requestRefreshToken)
				.map(refreshTokenService::verifyExpiration)
				.map(RefreshToken::getUser)
				.map(user -> {
					String token = jwtService.generateTokenFromUsername(user.getUsername());
					logger.info("Successfully refreshed access token for user: {}", user.getUsername());
					return new RefreshTokenResponse(token, requestRefreshToken);
				})
				.orElseThrow(() -> new ResourceNotFoundException("Refresh token is not present in database!"));
	}

	@Override
	public void logout(String refreshToken) {
		logger.info("Processing logout request for refresh token");
		if (refreshToken != null && !refreshToken.isBlank()) {
			refreshTokenService.deleteByToken(refreshToken);
		}
	}
}
