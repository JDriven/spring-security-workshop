package com.jdriven.leaverequest;

import java.time.LocalDate;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class LeaveRequest {

	private final UUID id = UUID.fromString("2a37e1b6-d7e3-45fd-8b50-59357425d62e"); // Hardcoded to match README curls

	private String employee;
	private LocalDate fromDate;
	private LocalDate toDate;

	@Builder.Default
	private Status status = Status.PENDING;

	public enum Status {
		PENDING, APPROVED, DENIED
	}

}
