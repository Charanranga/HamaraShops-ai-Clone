package com.hamarashops.authservice.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hamarashops.authservice.dto.UserResponse;
import com.hamarashops.authservice.model.User;
import com.hamarashops.authservice.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/users")
@Tag(name = "User Management API", description = "REST APIs for Managing System Users")
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@GetMapping
	@Operation(summary = "Get All Users", description = "Retrieve list of all registered users")
	public ResponseEntity<List<UserResponse>> getAllUsers() {
		logger.info("REST GET /users invoked");
		List<UserResponse> users = userService.getAllUsers();
		return new ResponseEntity<>(users, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	@Operation(summary = "Get User by ID", description = "Retrieve user details by primary key ID")
	public ResponseEntity<UserResponse> getUserById(@PathVariable Integer id) {
		logger.info("REST GET /users/{} invoked", id);
		UserResponse response = userService.getUserById(id);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PutMapping("/{id}")
	@Operation(summary = "Update User", description = "Update existing user information")
	public ResponseEntity<UserResponse> updateUser(@PathVariable Integer id, @RequestBody User userDetails) {
		logger.info("REST PUT /users/{} invoked", id);
		UserResponse response = userService.updateUser(id, userDetails);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Delete User", description = "Remove user record by primary key ID")
	public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
		logger.info("REST DELETE /users/{} invoked", id);
		userService.deleteUser(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
