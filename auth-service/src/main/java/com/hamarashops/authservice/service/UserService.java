package com.hamarashops.authservice.service;

import java.util.List;

import com.hamarashops.authservice.dto.UserResponse;
import com.hamarashops.authservice.model.User;

public interface UserService {

	List<UserResponse> getAllUsers();

	UserResponse getUserById(Integer id);

	UserResponse getUserByUsername(String username);

	UserResponse updateUser(Integer id, User userDetails);

	void deleteUser(Integer id);
}
