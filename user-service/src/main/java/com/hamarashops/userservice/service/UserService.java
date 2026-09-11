package com.hamarashops.userservice.service;

import java.util.List;

import com.hamarashops.userservice.dto.UserRequest;
import com.hamarashops.userservice.dto.UserResponse;

public interface UserService {

	UserResponse createUser(UserRequest request);

	List<UserResponse> getAllUsers();

	UserResponse getUserById(Integer id);

	UserResponse updateUser(Integer id, UserRequest request);

	void deleteUser(Integer id);
}
