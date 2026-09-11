package com.hamarashops.authservice.dto;

import jakarta.validation.constraints.NotBlank;

public class RefreshTokenRequest {

	@NotBlank(message = "Refresh token is required")
	private String refreshToken;

	public RefreshTokenRequest() {
		super();
	}

	public RefreshTokenRequest(String refreshToken) {
		super();
		this.refreshToken = refreshToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
}
