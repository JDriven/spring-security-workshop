package com.jdriven.gateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.header.XFrameOptionsServerHttpHeadersWriter.Mode;

@Configuration
public class SecurityConfig {

	@Bean
	SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
		// TODO Authenticate through configured OpenID Provider

		// TODO Configure logout to use the OpenID Connect specific success handler

		// Require authentication for all requests
		http.authorizeExchange().anyExchange().authenticated();
		// Allow showing /home within a frame
		http.headers().frameOptions().mode(Mode.SAMEORIGIN);
		// Disable CSRF in the gateway to prevent conflicts with proxied service CSRF
		http.csrf().disable();
		return http.build();
	}

}
