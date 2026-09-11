package com.hamarashops.authservice.service.impl;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hamarashops.authservice.exception.ResourceNotFoundException;
import com.hamarashops.authservice.model.RefreshToken;
import com.hamarashops.authservice.model.User;
import com.hamarashops.authservice.repo.RefreshTokenRepo;
import com.hamarashops.authservice.repo.UserRepo;
import com.hamarashops.authservice.service.RefreshTokenService;

@Service
@Transactional
public class RefreshTokenServiceImpl implements RefreshTokenService {

	private static final Logger logger = LoggerFactory.getLogger(RefreshTokenServiceImpl.class);

	@Autowired
	private RefreshTokenRepo refreshTokenRepo;

	@Autowired
	private UserRepo userRepo;

	@Value("${jwt.refresh-token.expiration-ms:604800000}")
	private Long refreshTokenDurationMs;

	@Override
	@Transactional(readOnly = true)
	public Optional<RefreshToken> findByToken(String token) {
		return refreshTokenRepo.findByToken(token);
	}

	@Override
	public RefreshToken createRefreshToken(String username) {
		User user = userRepo.findByUsernameOrEmail(username, username)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with username/email: " + username));

		Optional<RefreshToken> existingToken = refreshTokenRepo.findByUser(user);
		RefreshToken refreshToken;

		if (existingToken.isPresent()) {
			refreshToken = existingToken.get();
		} else {
			refreshToken = new RefreshToken();
			refreshToken.setUser(user);
		}

		refreshToken.setToken(UUID.randomUUID().toString());
		refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));

		logger.info("Generated refresh token for user: {}", user.getUsername());
		return refreshTokenRepo.save(refreshToken);
	}

	@Override
	public RefreshToken verifyExpiration(RefreshToken token) {
		if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
			refreshTokenRepo.delete(token);
			logger.warn("Refresh token expired and deleted for user: {}", token.getUser().getUsername());
			throw new RuntimeException("Refresh token was expired. Please make a new signin request");
		}
		return token;
	}

	@Override
	public int deleteByUserId(Integer userId) {
		User user = userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
		return refreshTokenRepo.deleteByUser(user);
	}

	@Override
	public void deleteByToken(String token) {
		Optional<RefreshToken> refreshToken = refreshTokenRepo.findByToken(token);
		refreshToken.ifPresent(refreshTokenRepo::delete);
	}
}
