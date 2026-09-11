package com.hamarashops.authservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.hamarashops.authservice.dto.LoginRequest;
import com.hamarashops.authservice.dto.LoginResponse;
import com.hamarashops.authservice.dto.RegisterRequest;
import com.hamarashops.authservice.dto.RegisterResponse;
import com.hamarashops.authservice.exception.DuplicateUserException;
import com.hamarashops.authservice.model.RefreshToken;
import com.hamarashops.authservice.model.Role;
import com.hamarashops.authservice.model.User;
import com.hamarashops.authservice.repo.RoleRepo;
import com.hamarashops.authservice.repo.UserRepo;
import com.hamarashops.authservice.service.impl.AuthServiceImpl;
import com.hamarashops.authservice.client.AuditServiceClient;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

	@Mock
	private UserRepo userRepo;

	@Mock
	private RoleRepo roleRepo;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private AuthenticationManager authenticationManager;

	@Mock
	private JwtService jwtService;

	@Mock
	private RefreshTokenService refreshTokenService;

	@Mock
	private Authentication authentication;

	@Mock
	private AuditServiceClient auditServiceClient;

	@InjectMocks
	private AuthServiceImpl authService;

	private User user;
	private Role role;
	private RegisterRequest registerRequest;
	private LoginRequest loginRequest;

	@BeforeEach
	void setUp() {
		role = new Role(1, "ROLE_USER");
		user = new User(1, "John Doe", "johndoe", "john@example.com", "encodedPassword", "9876543210", true, role);

		registerRequest = new RegisterRequest();
		registerRequest.setFullName("John Doe");
		registerRequest.setUsername("johndoe");
		registerRequest.setEmail("john@example.com");
		registerRequest.setPassword("plainPassword");
		registerRequest.setPhone("9876543210");

		loginRequest = new LoginRequest("johndoe", "plainPassword");
	}

	@Test
	void testRegister_Success() {
		when(userRepo.existsByUsername("johndoe")).thenReturn(false);
		when(userRepo.existsByEmail("john@example.com")).thenReturn(false);
		when(roleRepo.findByRoleName("ROLE_USER")).thenReturn(Optional.of(role));
		when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");
		when(userRepo.save(any(User.class))).thenReturn(user);

		RegisterResponse response = authService.register(registerRequest);

		assertNotNull(response);
		assertEquals("johndoe", response.getUsername());
		assertEquals("john@example.com", response.getEmail());
		assertEquals("ROLE_USER", response.getRole());
		verify(userRepo).save(any(User.class));
		verify(auditServiceClient).logAudit(any(), any(), any(), any(), any(), any());
	}

	@Test
	void testRegister_DuplicateUsername_ThrowsException() {
		when(userRepo.existsByUsername("johndoe")).thenReturn(true);

		assertThrows(DuplicateUserException.class, () -> {
			authService.register(registerRequest);
		});
	}

	@Test
	void testLogin_Success() {
		RefreshToken refreshToken = new RefreshToken(1, "sample-refresh-token", user, Instant.now().plusSeconds(3600));

		when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
		when(authentication.getName()).thenReturn("johndoe");
		when(jwtService.generateToken(authentication)).thenReturn("sample-jwt-token");
		when(refreshTokenService.createRefreshToken("johndoe")).thenReturn(refreshToken);
		when(userRepo.findByUsernameOrEmail("johndoe", "johndoe")).thenReturn(Optional.of(user));

		LoginResponse response = authService.login(loginRequest);

		assertNotNull(response);
		assertEquals("sample-jwt-token", response.getAccessToken());
		assertEquals("sample-refresh-token", response.getRefreshToken());
		assertEquals("johndoe", response.getUsername());
		verify(auditServiceClient).logAudit(any(), any(), any(), any(), any(), any());
	}
}
