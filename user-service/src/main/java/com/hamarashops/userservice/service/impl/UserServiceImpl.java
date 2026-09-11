package com.hamarashops.userservice.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hamarashops.userservice.dto.UserRequest;
import com.hamarashops.userservice.dto.UserResponse;
import com.hamarashops.userservice.exception.DuplicateUserException;
import com.hamarashops.userservice.exception.ResourceNotFoundException;
import com.hamarashops.userservice.model.User;
import com.hamarashops.userservice.repo.UserRepo;
import com.hamarashops.userservice.service.UserService;
import com.hamarashops.userservice.client.AuditServiceClient;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private AuditServiceClient auditServiceClient;

	@Override
	public UserResponse createUser(UserRequest request) {
		logger.info("Creating user with email: {}", request.getEmail());
		if (userRepo.existsByEmail(request.getEmail())) {
			throw new DuplicateUserException("User with email '" + request.getEmail() + "' already exists!");
		}

		User user = new User();
		user.setFullName(request.getFullName());
		user.setEmail(request.getEmail());
		user.setPhone(request.getPhone());
		user.setAddress(request.getAddress());

		User saved = userRepo.save(user);
		logger.info("Successfully created user ID: {}", saved.getId());

		try {
			auditServiceClient.logAudit(
					saved.getId(),
					"Create User",
					"User",
					String.valueOf(saved.getId()),
					"Successfully created user: " + saved.getEmail(),
					"SUCCESS"
			);
		} catch (Exception e) {
			logger.warn("Failed to log audit event: {}", e.getMessage());
		}

		return mapToResponse(saved);
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserResponse> getAllUsers() {
		logger.info("Fetching all users");
		return userRepo.findAll().stream()
				.map(this::mapToResponse)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public UserResponse getUserById(Integer id) {
		logger.info("Fetching user by ID: {}", id);
		User user = userRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
		return mapToResponse(user);
	}

	@Override
	public UserResponse updateUser(Integer id, UserRequest request) {
		logger.info("Updating user ID: {}", id);
		User user = userRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));

		if (request.getFullName() != null) {
			user.setFullName(request.getFullName());
		}
		if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
			if (userRepo.existsByEmail(request.getEmail())) {
				throw new DuplicateUserException("Email '" + request.getEmail() + "' is already in use!");
			}
			user.setEmail(request.getEmail());
		}
		if (request.getPhone() != null) {
			user.setPhone(request.getPhone());
		}
		if (request.getAddress() != null) {
			user.setAddress(request.getAddress());
		}

		User updated = userRepo.save(user);
		logger.info("Successfully updated user ID: {}", updated.getId());

		try {
			auditServiceClient.logAudit(
					updated.getId(),
					"Update User",
					"User",
					String.valueOf(updated.getId()),
					"Successfully updated user: " + updated.getEmail(),
					"SUCCESS"
			);
		} catch (Exception e) {
			logger.warn("Failed to log audit event: {}", e.getMessage());
		}

		return mapToResponse(updated);
	}

	@Override
	public void deleteUser(Integer id) {
		logger.info("Deleting user ID: {}", id);
		if (!userRepo.existsById(id)) {
			throw new ResourceNotFoundException("Cannot delete. User not found with ID: " + id);
		}
		userRepo.deleteById(id);
		logger.info("Successfully deleted user ID: {}", id);

		try {
			auditServiceClient.logAudit(
					id,
					"Delete User",
					"User",
					String.valueOf(id),
					"Successfully deleted user ID: " + id,
					"SUCCESS"
			);
		} catch (Exception e) {
			logger.warn("Failed to log audit event: {}", e.getMessage());
		}
	}

	private UserResponse mapToResponse(User user) {
		UserResponse response = new UserResponse();
		response.setId(user.getId());
		response.setFullName(user.getFullName());
		response.setEmail(user.getEmail());
		response.setPhone(user.getPhone());
		response.setAddress(user.getAddress());
		response.setCreatedAt(user.getCreatedAt());
		response.setUpdatedAt(user.getUpdatedAt());
		return response;
	}
}
