package com.hamarashops.apigateway.config;

import static org.springframework.cloud.gateway.server.mvc.filter.FilterFunctions.stripPrefix;
import static org.springframework.cloud.gateway.server.mvc.filter.FilterFunctions.uri;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.cloud.gateway.server.mvc.predicate.GatewayRequestPredicates.path;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

/**
 * Cloud Gateway Routing Configuration for Cloud Run environments.
 * Uses direct absolute service URLs via FilterFunctions.uri().
 */
@Configuration
@Profile("cloud")
public class CloudGatewayConfig {

	@Value("${auth.service.url}")
	private String authServiceUrl;

	@Value("${user.service.url}")
	private String userServiceUrl;

	@Value("${candidate.service.url}")
	private String candidateServiceUrl;

	@Value("${document.service.url}")
	private String documentServiceUrl;

	@Value("${audit.service.url}")
	private String auditServiceUrl;

	@Bean
	public RouterFunction<ServerResponse> authServiceRoute() {
		return route("auth-service")
				.route(path("/api/auth/**"), http())
				.filter(uri(authServiceUrl))
				.filter(stripPrefix(1))
				.build();
	}

	@Bean
	public RouterFunction<ServerResponse> userServiceRoute() {
		return route("user-service")
				.route(path("/api/users/**"), http())
				.filter(uri(userServiceUrl))
				.filter(stripPrefix(1))
				.build();
	}

	@Bean
	public RouterFunction<ServerResponse> candidateServiceRoute() {
		return route("candidate-service")
				.route(path("/api/candidates/**"), http())
				.filter(uri(candidateServiceUrl))
				.filter(stripPrefix(1))
				.build();
	}

	@Bean
	public RouterFunction<ServerResponse> documentServiceRoute() {
		return route("document-service")
				.route(path("/api/documents/**"), http())
				.filter(uri(documentServiceUrl))
				.filter(stripPrefix(1))
				.build();
	}

	@Bean
	public RouterFunction<ServerResponse> auditServiceRoute() {
		return route("audit-service")
				.route(path("/api/audit/**"), http())
				.filter(uri(auditServiceUrl))
				.filter(stripPrefix(1))
				.build();
	}
}
