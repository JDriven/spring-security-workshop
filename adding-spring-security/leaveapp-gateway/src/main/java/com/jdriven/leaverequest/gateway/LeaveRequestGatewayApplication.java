package com.jdriven.leaverequest.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.oidc.web.server.logout.OidcClientInitiatedServerLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;

@SpringBootApplication
public class LeaveRequestGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(LeaveRequestGatewayApplication.class, args);
	}
}

@Configuration
class SecurityConfig {

	@Bean
	SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http,
			ReactiveClientRegistrationRepository clientRegistrationRepository) {
		// Authenticate through configured OpenID Provider
		http.oauth2Login();
		// Also logout at the OpenID Connect provider
		http.logout(logout -> logout.logoutSuccessHandler(new OidcClientInitiatedServerLogoutSuccessHandler(
				clientRegistrationRepository)));
		// Require authentication for all requests
		http.authorizeExchange().anyExchange().authenticated();
		return http.build();
	}

}
