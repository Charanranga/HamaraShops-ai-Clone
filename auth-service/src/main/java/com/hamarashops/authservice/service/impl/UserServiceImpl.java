package com.hamarashops.authservice.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hamarashops.authservice.dto.UserResponse;
import com.hamarashops.authservice.exception.ResourceNotFoundException;
import com.hamarashops.authservice.model.User;
import com.hamarashops.authservice.repo.UserRepo;
import com.hamarashops.authservice.service.UserService;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	@Transactional(readOnly = true)
	public List<UserResponse> getAllUsers() {
		logger.info("Fetching all registered users");
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
	@Transactional(readOnly = true)
	public UserResponse getUserByUsername(String username) {
		logger.info("Fetching user by username: {}", username);
		User user = userRepo.findByUsername(username)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
		return mapToResponse(user);
	}

	@Override
	public UserResponse updateUser(Integer id, User userDetails) {
		logger.info("Updating user ID: {}", id);
		User user = userRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));

		if (userDetails.getFullName() != null) {
			user.setFullName(userDetails.getFullName());
		}
		if (userDetails.getPhone() != null) {
			user.setPhone(userDetails.getPhone());
		}
		if (userDetails.getPassword() != null && !userDetails.getPassword().isBlank()) {
			user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
		}
		if (userDetails.getEnabled() != null) {
			user.setEnabled(userDetails.getEnabled());
		}

		User updated = userRepo.save(user);
		logger.info("Successfully updated user ID: {}", updated.getId());
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
	}

	private UserResponse mapToResponse(User user) {
		UserResponse response = new UserResponse();
		response.setId(user.getId());
		response.setFullName(user.getFullName());
		response.setUsername(user.getUsername());
		response.setEmail(user.getEmail());
		response.setPhone(user.getPhone());
		response.setEnabled(user.getEnabled());
		response.setRole(user.getRole() != null ? user.getRole().getRoleName() : "ROLE_USER");
		response.setCreatedAt(user.getCreatedAt());
		response.setUpdatedAt(user.getUpdatedAt());
		return response;
	}
}
