package com.jdriven.service;

import java.util.Collections;

import com.jdriven.access.SpreadsheetAccess;
import com.jdriven.access.SpreadsheetAccessStore;
import com.jdriven.model.Spreadsheet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class SpreadsheetServiceIntegrationTest {

	@Autowired
	private SpreadsheetAccessStore store;

	@Autowired
	private SpreadsheetService service;

	private Spreadsheet spreadsheet;

	@BeforeEach
	void setup() {
		User alice = new User("alice", "", Collections.emptyList());
		User bob = new User("bob", "", Collections.emptyList());
		spreadsheet = new Spreadsheet(123L, "alice's spreadsheet");
		store.getAccess().add(new SpreadsheetAccess(alice, spreadsheet));
		store.getAccess().add(new SpreadsheetAccess(bob, spreadsheet));
	}

	@Test
	@WithMockUser("alice")
	void testAliceAllowedToReadSpreadsheet() {
		service.read(spreadsheet);
	}

	@Test
	@WithMockUser("alice")
	void testAliceNotAllowedToReadAnotherSpreadsheet() {
		Spreadsheet anotherSpreadsheet = new Spreadsheet(345L, "another spreadsheet");
		assertThrows(AccessDeniedException.class, () -> service.read(anotherSpreadsheet));
	}

	@Test
	@WithMockUser("bob")
	void testBobAllowedToReadSpreadsheet() {
		service.read(spreadsheet);
	}

	@Test
	@WithMockUser("eve")
	void testEveNotAllowedToReadSpreadsheet() {
		assertThrows(AccessDeniedException.class, () -> service.read(spreadsheet));
	}

}
