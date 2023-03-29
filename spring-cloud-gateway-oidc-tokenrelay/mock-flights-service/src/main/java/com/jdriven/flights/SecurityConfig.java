package com.jdriven.flights;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		// TODO Validate tokens through configured OpenID Provider (also requires application.yml changes)

		// Require authentication for all requests
		http.authorizeHttpRequests(auth -> auth.anyRequest().authenticated());

		// Allow showing pages within a frame
		http.headers().frameOptions().sameOrigin();

		return http.build();
	}

	@Bean
	JwtDecoder jwtDecoderByIssuerUri(OAuth2ResourceServerProperties properties) {
		String issuerUri = properties.getJwt().getIssuerUri();
		NimbusJwtDecoder jwtDecoder = (NimbusJwtDecoder) JwtDecoders.fromIssuerLocation(issuerUri);
		jwtDecoder.setClaimSetConverter(new UsernameSubClaimAdapter());
		return jwtDecoder;
	}

}

// this part configures the extraction of custom roles from the JWT

//TODO: configure the use of this converter
class KeycloakRealmRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

	@Override
	public Collection<GrantedAuthority> convert(Jwt jwt) {
		// TODO Convert realm_access.roles claims to granted authorities, for further use in access decisions (RBAC)
		return Collections.emptyList();
	}

}

// this part configures the extraction of the username from the JWT

class UsernameSubClaimAdapter implements Converter<Map<String, Object>, Map<String, Object>> {

	private final MappedJwtClaimSetConverter delegate = MappedJwtClaimSetConverter
			.withDefaults(Collections.emptyMap());

	@Override
	public Map<String, Object> convert(Map<String, Object> claims) {
		Map<String, Object> convertedClaims = this.delegate.convert(claims);
		//TODO: manually map another claim to sub to prevent the user UUID being used as authentication name
		convertedClaims.put("sub", convertedClaims.get("sub"));
		return convertedClaims;
	}

}
