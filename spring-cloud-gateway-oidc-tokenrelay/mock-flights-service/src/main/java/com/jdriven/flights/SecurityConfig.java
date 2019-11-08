package com.jdriven.flights;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// TODO Validate tokens through configured OpenID Provider
		// TODO Convert realm_access.roles claims to granted authorities, for use in access decisions
		// TODO Use preferred_username from claims as authentication name, instead of UUID subject

		// Require authentication for all requests
		http.authorizeRequests().anyRequest().authenticated();

		// Allow showing pages within a frame
		http.headers().frameOptions().sameOrigin();
	}

}
