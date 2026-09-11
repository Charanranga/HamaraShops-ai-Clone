package com.hamarashops.apigateway.config;

import static org.springframework.cloud.gateway.server.mvc.filter.FilterFunctions.stripPrefix;
import static org.springframework.cloud.gateway.server.mvc.filter.LoadBalancerFilterFunctions.lb;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.cloud.gateway.server.mvc.predicate.GatewayRequestPredicates.path;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import org.springframework.context.annotation.Profile;

/**
 * Infrastructure Gateway Configuration for HamaraShops Platform.
 */
@Configuration
@Profile("!cloud")
public class GatewayConfig {

	@Bean
	public RouterFunction<ServerResponse> authServiceRoute() {
		return route("auth-service")
				.route(path("/api/auth/**"), http())
				.filter(lb("AUTH-SERVICE"))
				.filter(stripPrefix(1))
				.build();
	}

	@Bean
	public RouterFunction<ServerResponse> userServiceRoute() {
		return route("user-service")
				.route(path("/api/users/**"), http())
				.filter(lb("USER-SERVICE"))
				.filter(stripPrefix(1))
				.build();
	}

	@Bean
	public RouterFunction<ServerResponse> candidateServiceRoute() {
		return route("candidate-service")
				.route(path("/api/candidates/**"), http())
				.filter(lb("CANDIDATE-SERVICE"))
				.filter(stripPrefix(1))
				.build();
	}

	@Bean
	public RouterFunction<ServerResponse> documentServiceRoute() {
		return route("document-service")
				.route(path("/api/documents/**"), http())
				.filter(lb("DOCUMENT-SERVICE"))
				.filter(stripPrefix(1))
				.build();
	}

	@Bean
	public RouterFunction<ServerResponse> auditServiceRoute() {
		return route("audit-service")
				.route(path("/api/audit/**"), http())
				.filter(lb("AUDIT-SERVICE"))
				.filter(stripPrefix(1))
				.build();
	}
}
