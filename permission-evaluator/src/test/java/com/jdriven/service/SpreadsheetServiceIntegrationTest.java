package com.jdriven.service;

import java.util.Collections;

import com.jdriven.model.Spreadsheet;
import com.jdriven.permission.SpreadsheetPermission;
import com.jdriven.permission.SpreadsheetPermissionStore;

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
	private SpreadsheetPermissionStore store;

	@Autowired
	private SpreadsheetService service;

	private Spreadsheet spreadsheet;

	@BeforeEach
	void setup() {
		User alice = new User("alice", "", Collections.emptyList());
		User bob = new User("bob", "", Collections.emptyList());
		spreadsheet = new Spreadsheet(123L, "alice's spreadsheet");
		store.getPermissions().add(new SpreadsheetPermission(alice, spreadsheet, "READ"));
		store.getPermissions().add(new SpreadsheetPermission(alice, spreadsheet, "WRITE"));
		store.getPermissions().add(new SpreadsheetPermission(alice, spreadsheet, "PRINT"));
		store.getPermissions().add(new SpreadsheetPermission(bob, spreadsheet, "READ"));
	}

	@Test
	@WithMockUser("alice")
	void testAliceAllowedToReadSpreadsheet() {
		service.read(spreadsheet);
	}

	@Test
	@WithMockUser("alice")
	void testAliceAllowedToReadSpreadsheetById() {
		service.readById(spreadsheet.getId());
	}

	@Test
	@WithMockUser("alice")
	void testAliceAllowedToWriteSpreadsheet() {
		service.write(spreadsheet);
	}

	@Test
	@WithMockUser("alice")
	void testAliceAllowedToWriteSpreadsheetById() {
		service.writeById(spreadsheet.getId());
	}

	@Test
	@WithMockUser("alice")
	void testAliceAllowedToPrintSpreadsheet() {
		service.print(spreadsheet);
	}

	@Test
	@WithMockUser("alice")
	void testAliceNotAllowedToReadAnotherSpreadsheet() {
		Spreadsheet anotherSpreadsheet = new Spreadsheet(456L, "another spreadsheet");
		assertThrows(AccessDeniedException.class, () -> service.read(anotherSpreadsheet));
	}

	@Test
	@WithMockUser("alice")
	void testAliceNotAllowedToReadAnotherSpreadsheetById() {
		assertThrows(AccessDeniedException.class, () -> service.readById(456L));
	}

	@Test
	@WithMockUser("bob")
	void testBobAllowedToReadSpreadsheet() {
		service.read(spreadsheet);
	}

	@Test
	@WithMockUser("bob")
	void testBobAllowedToReadSpreadsheetById() {
		service.readById(spreadsheet.getId());
	}

	@Test
	@WithMockUser("bob")
	void testBobNotAllowedToWriteSpreadsheet() {
		assertThrows(AccessDeniedException.class, () -> service.write(spreadsheet));
	}

	@Test
	@WithMockUser("bob")
	void testBobNotAllowedToPrintSpreadsheet() {
		assertThrows(AccessDeniedException.class, () -> service.print(spreadsheet));
	}

	@Test
	@WithMockUser("bob")
	void testBobNotAllowedToWriteSpreadsheetById() {
		assertThrows(AccessDeniedException.class, () -> service.writeById(spreadsheet.getId()));
	}

	@Test
	@WithMockUser("eve")
	void testEveNotAllowedToReadSpreadsheet() {
		assertThrows(AccessDeniedException.class, () -> service.read(spreadsheet));
	}

	@Test
	@WithMockUser("eve")
	void testEveNotAllowedToReadSpreadsheetById() {
		assertThrows(AccessDeniedException.class, () -> service.readById(spreadsheet.getId()));
	}
}
