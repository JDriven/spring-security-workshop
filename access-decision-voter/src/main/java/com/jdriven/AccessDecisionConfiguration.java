package com.jdriven;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@Configuration
public class AccessDecisionConfiguration extends GlobalMethodSecurityConfiguration {
	// TODO: Have the @Secured annotation on SpreadsheetService#read(Spreadsheet) check access against permissions stored
	// in SpreadsheetAccessStore
}
