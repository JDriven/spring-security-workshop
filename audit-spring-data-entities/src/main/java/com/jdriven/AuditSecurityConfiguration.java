package com.jdriven;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class AuditSecurityConfiguration {
	// TODO Configure JPA to look up the Author corresponding to the active user and store that along with any BlogPost
}
