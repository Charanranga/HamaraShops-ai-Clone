package com.hamarashops.authservice.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hamarashops.authservice.model.RefreshToken;
import com.hamarashops.authservice.model.User;

@Repository
public interface RefreshTokenRepo extends JpaRepository<RefreshToken, Integer> {

	Optional<RefreshToken> findByToken(String token);

	Optional<RefreshToken> findByUser(User user);

	int deleteByUser(User user);
}
