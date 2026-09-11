package com.hamarashops.authservice.service;

import com.hamarashops.authservice.dto.LoginRequest;
import com.hamarashops.authservice.dto.LoginResponse;
import com.hamarashops.authservice.dto.RefreshTokenRequest;
import com.hamarashops.authservice.dto.RefreshTokenResponse;
import com.hamarashops.authservice.dto.RegisterRequest;
import com.hamarashops.authservice.dto.RegisterResponse;

public interface AuthService {

	RegisterResponse register(RegisterRequest request);

	LoginResponse login(LoginRequest request);

	RefreshTokenResponse refreshToken(RefreshTokenRequest request);

	void logout(String refreshToken);
}
