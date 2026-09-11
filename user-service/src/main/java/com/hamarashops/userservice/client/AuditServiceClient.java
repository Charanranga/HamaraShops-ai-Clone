package com.hamarashops.userservice.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.hamarashops.userservice.dto.AuditLogRequest;

@Component
public class AuditServiceClient {

	private static final Logger logger = LoggerFactory.getLogger(AuditServiceClient.class);

	@Value("${audit.service.url:http://AUDIT-SERVICE/audit}")
	private String auditServiceUrl;

	@Autowired
	private RestTemplate loadBalancedRestTemplate;

	@Autowired
	@Qualifier("plainRestTemplate")
	private RestTemplate plainRestTemplate;

	public void logAudit(Integer userId, String action, String entityName, String entityId, String description, String status) {
		AuditLogRequest request = new AuditLogRequest(
				userId,
				"USER-SERVICE",
				action,
				entityName,
				entityId,
				description,
				"127.0.0.1",
				status
		);

		// 1. Try Eureka LoadBalanced RestTemplate
		try {
			logger.info("Attempting Eureka LoadBalanced audit log to Audit Service ({})", auditServiceUrl);
			loadBalancedRestTemplate.postForEntity(auditServiceUrl, request, Object.class);
			logger.info("Successfully sent audit log to auditdb via LoadBalanced Audit Service call");
			return;
		} catch (Exception e) {
			logger.warn("LoadBalanced call to Audit Service failed ({}), trying direct HTTP fallback...", e.getMessage());
		}

		// 2. Direct HTTP Fallback
		try {
			String directUrl = "http://localhost:8085/audit";
			logger.info("Attempting Direct HTTP audit log to Audit Service ({})", directUrl);
			plainRestTemplate.postForEntity(directUrl, request, Object.class);
			logger.info("Successfully sent audit log to auditdb via Direct HTTP Audit Service call");
		} catch (Exception ex) {
			logger.error("Failed to send audit log to Audit Service via both Eureka and Direct HTTP: {}", ex.getMessage(), ex);
		}
	}
}
