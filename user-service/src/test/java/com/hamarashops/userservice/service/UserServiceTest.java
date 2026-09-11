package com.hamarashops.userservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hamarashops.userservice.dto.UserRequest;
import com.hamarashops.userservice.dto.UserResponse;
import com.hamarashops.userservice.exception.DuplicateUserException;
import com.hamarashops.userservice.exception.ResourceNotFoundException;
import com.hamarashops.userservice.model.User;
import com.hamarashops.userservice.repo.UserRepo;
import com.hamarashops.userservice.service.impl.UserServiceImpl;
import com.hamarashops.userservice.client.AuditServiceClient;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	@Mock
	private UserRepo userRepo;

	@Mock
	private AuditServiceClient auditServiceClient;

	@InjectMocks
	private UserServiceImpl userService;

	private User user;
	private UserRequest userRequest;

	@BeforeEach
	void setUp() {
		user = new User(1, "Jane Doe", "jane@example.com", "9876543210", "123 Main St");
		userRequest = new UserRequest("Jane Doe", "jane@example.com", "9876543210", "123 Main St");
	}

	@Test
	void testCreateUser_Success() {
		when(userRepo.existsByEmail("jane@example.com")).thenReturn(false);
		when(userRepo.save(any(User.class))).thenReturn(user);

		UserResponse response = userService.createUser(userRequest);

		assertNotNull(response);
		assertEquals("Jane Doe", response.getFullName());
		assertEquals("jane@example.com", response.getEmail());
		verify(userRepo).save(any(User.class));
		verify(auditServiceClient).logAudit(any(), any(), any(), any(), any(), any());
	}

	@Test
	void testCreateUser_DuplicateEmail_ThrowsException() {
		when(userRepo.existsByEmail("jane@example.com")).thenReturn(true);

		assertThrows(DuplicateUserException.class, () -> {
			userService.createUser(userRequest);
		});
	}

	@Test
	void testGetUserById_Success() {
		when(userRepo.findById(1)).thenReturn(Optional.of(user));

		UserResponse response = userService.getUserById(1);

		assertNotNull(response);
		assertEquals(1, response.getId());
		assertEquals("Jane Doe", response.getFullName());
	}

	@Test
	void testGetUserById_NotFound_ThrowsException() {
		when(userRepo.findById(99)).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> {
			userService.getUserById(99);
		});
	}

	@Test
	void testGetAllUsers() {
		when(userRepo.findAll()).thenReturn(List.of(user));

		List<UserResponse> users = userService.getAllUsers();

		assertNotNull(users);
		assertEquals(1, users.size());
	}
}
