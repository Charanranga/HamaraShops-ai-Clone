package com.hamarashops.authservice.service;

import java.util.Optional;

import com.hamarashops.authservice.model.RefreshToken;

public interface RefreshTokenService {

	Optional<RefreshToken> findByToken(String token);

	RefreshToken createRefreshToken(String username);

	RefreshToken verifyExpiration(RefreshToken token);

	int deleteByUserId(Integer userId);

	void deleteByToken(String token);
}
