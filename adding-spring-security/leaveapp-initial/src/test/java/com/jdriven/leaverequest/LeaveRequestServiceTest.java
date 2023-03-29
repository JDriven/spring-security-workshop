package com.jdriven.leaverequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static com.jdriven.leaverequest.LeaveRequest.Status.APPROVED;
import static com.jdriven.leaverequest.LeaveRequest.Status.DENIED;
import static com.jdriven.leaverequest.LeaveRequest.Status.PENDING;
import static java.time.LocalDate.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class LeaveRequestServiceTest {

	@SpyBean
	private LeaveRequestRepository repository;

	@Autowired
	private LeaveRequestService service;

	@BeforeEach
	void beforeEach() {
		repository.clear();
	}

	@Nested
	class AuthorizeUser {

		// TODO Authenticate as Alice when making these requests

		@Test
		void testRequest() {
			LeaveRequest leaveRequest = service.request("alice", of(2022, 11, 30), of(2022, 12, 03));
			verify(repository).save(leaveRequest);
		}

		@Test
		void testRetrieveById() {
			LeaveRequest saved = repository
					.save(new LeaveRequest("alice", of(2022, 11, 30), of(2022, 12, 03), PENDING));
			Optional<LeaveRequest> retrieved = service.retrieve(saved.getId());
			verify(repository).findById(saved.getId());
			assertThat(retrieved).isPresent();
			assertThat(retrieved).get().isSameAs(saved);
		}

		@Test
		void testRetrieveByIdMissing() {
			UUID randomUUID = UUID.randomUUID();
			Optional<LeaveRequest> retrieved = service.retrieve(randomUUID);
			verify(repository).findById(randomUUID);
			assertThat(retrieved).isEmpty();
		}

		@Test
		void testRetrieveForEmployee() {
			LeaveRequest saved = repository
					.save(new LeaveRequest("alice", of(2022, 11, 30), of(2022, 12, 3), PENDING));
			List<LeaveRequest> retrieved = service.retrieveFor("alice");
			verify(repository).findByEmployee("alice");
			assertThat(retrieved).containsExactly(saved);
		}

	}

	@Nested
	class AuthorizeRole {

		// TODO Authenticate with HR role when making these requests

		@Test
		void testRequest() {
			LeaveRequest leaveRequest = service.request("alice", of(2022, 11, 30), of(2022, 12, 03));
			verify(repository).save(leaveRequest);
		}

		@Test
		void testRetrieveById() {
			LeaveRequest saved = repository
					.save(new LeaveRequest("alice", of(2022, 11, 30), of(2022, 12, 03), PENDING));
			Optional<LeaveRequest> retrieved = service.retrieve(saved.getId());
			verify(repository).findById(saved.getId());
			assertThat(retrieved).isPresent();
			assertThat(retrieved).get().isSameAs(saved);
		}

		@Test
		void testRetrieveForEmployee() {
			LeaveRequest saved = repository
					.save(new LeaveRequest("alice", of(2022, 11, 30), of(2022, 12, 03), PENDING));
			List<LeaveRequest> retrieved = service.retrieveFor("alice");
			verify(repository).findByEmployee("alice");
			assertThat(retrieved).containsExactly(saved);
		}

		@Test
		void testApprove() {
			LeaveRequest saved = repository
					.save(new LeaveRequest("alice", of(2022, 11, 30), of(2022, 12, 03), PENDING));
			Optional<LeaveRequest> approved = service.approve(saved.getId());
			verify(repository).findById(saved.getId());
			assertThat(approved).isPresent();
			assertThat(approved).get().isSameAs(saved);
			assertThat(approved.get().getStatus()).isSameAs(APPROVED);
		}

		@Test
		void testDeny() {
			LeaveRequest saved = repository
					.save(new LeaveRequest("alice", of(2022, 11, 30), of(2022, 12, 03), PENDING));
			Optional<LeaveRequest> denied = service.deny(saved.getId());
			verify(repository).findById(saved.getId());
			assertThat(denied).isPresent();
			assertThat(denied).get().isSameAs(saved);
			assertThat(denied.get().getStatus()).isSameAs(DENIED);
		}

		@Test
		void testRetrieveAll() {
			LeaveRequest saved = repository
					.save(new LeaveRequest("alice", of(2022, 11, 30), of(2022, 12, 03), PENDING));
			List<LeaveRequest> denied = service.retrieveAll();
			verify(repository).findAll();
			assertThat(denied).containsExactly(saved);
		}

	}

}
