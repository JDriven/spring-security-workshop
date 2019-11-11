package com.jdriven.hotels;

import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// TODO Validate tokens through configured OpenID Provider (also requires application.yml changes)

		// Require authentication for all requests
		http.authorizeRequests().anyRequest().authenticated();

		// Allow showing pages within a frame
		http.headers().frameOptions().sameOrigin();
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

	@Bean
	public JwtDecoder jwtDecoderByIssuerUri(OAuth2ResourceServerProperties properties) {
		String issuerUri = properties.getJwt().getIssuerUri();
		NimbusJwtDecoder jwtDecoder = (NimbusJwtDecoder) JwtDecoders.fromIssuerLocation(issuerUri);
		jwtDecoder.setClaimSetConverter(new UsernameSubClaimAdapter());
		return jwtDecoder;
	}

	class UsernameSubClaimAdapter implements Converter<Map<String, Object>, Map<String, Object>> {

		private final MappedJwtClaimSetConverter delegate = MappedJwtClaimSetConverter.withDefaults(Collections.emptyMap());

		@Override
		public Map<String, Object> convert(Map<String, Object> claims) {
			Map<String, Object> convertedClaims = this.delegate.convert(claims);
			//TODO: manually map another claim to sub to prevent the user UUID being used as authentication name
			convertedClaims.put("sub", convertedClaims.get("sub"));
			return convertedClaims;
		}

	}

}
