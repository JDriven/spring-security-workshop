package com.jdriven;

import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {
	// TODO Have the @Secured annotation on SpreadsheetService#read(Spreadsheet) check access against permissions stored
	// in SpreadsheetAccessStore
}
