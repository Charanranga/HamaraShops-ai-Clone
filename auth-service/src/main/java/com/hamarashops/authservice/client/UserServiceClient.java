package com.hamarashops.authservice.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.hamarashops.authservice.dto.UserCreateRequest;

@Component
public class UserServiceClient {

	private static final Logger logger = LoggerFactory.getLogger(UserServiceClient.class);

	@Value("${user.service.url:http://USER-SERVICE/users}")
	private String userServiceUrl;

	@Autowired
	private RestTemplate loadBalancedRestTemplate;

	@Autowired
	@Qualifier("plainRestTemplate")
	private RestTemplate plainRestTemplate;

	public void createUserProfile(String fullName, String email, String phone, String address) {
		UserCreateRequest request = new UserCreateRequest(
				fullName,
				email,
				phone != null ? phone : "",
				address != null ? address : ""
		);

		// 1. Try Eureka LoadBalanced RestTemplate
		try {
			logger.info("Attempting Eureka LoadBalanced call to User Service ({}) for email: {}", userServiceUrl, email);
			loadBalancedRestTemplate.postForEntity(userServiceUrl, request, Object.class);
			logger.info("Successfully created user profile in userdb via LoadBalanced User Service call for email: {}", email);
			return;
		} catch (Exception e) {
			logger.warn("LoadBalanced call to User Service failed ({}), trying direct HTTP fallback...", e.getMessage());
		}

		// 2. Direct HTTP Fallback
		try {
			String directUrl = "http://localhost:8082/users";
			logger.info("Attempting Direct HTTP call to User Service ({}) for email: {}", directUrl, email);
			plainRestTemplate.postForEntity(directUrl, request, Object.class);
			logger.info("Successfully created user profile in userdb via Direct HTTP User Service call for email: {}", email);
		} catch (Exception ex) {
			logger.error("CRITICAL: Failed to create user profile in User Service via both Eureka and Direct HTTP: {}", ex.getMessage(), ex);
		}
	}
}
