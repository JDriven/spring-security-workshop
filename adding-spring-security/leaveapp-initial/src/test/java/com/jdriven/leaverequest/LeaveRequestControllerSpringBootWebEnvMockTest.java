package com.jdriven.leaverequest;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.jdriven.leaverequest.LeaveRequest.Status.APPROVED;
import static com.jdriven.leaverequest.LeaveRequest.Status.PENDING;
import static java.time.LocalDate.of;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
class LeaveRequestControllerSpringBootWebEnvMockTest {

	@Autowired
	private LeaveRequestRepository repository;

	@Autowired
	private MockMvc mockmvc;

	@BeforeEach
	void beforeEach() {
		repository.clear();
	}

	@Nested
	class AuthorizeUser {

		// TODO Authenticate as user alice when making these requests

		@Test
		void testRequest() throws Exception {
			mockmvc.perform(post("/request/{employee}", "alice")
					.param("from", "2022-11-30")
					.param("to", "2022-12-03"))
					.andExpectAll(
							status().isAccepted(),
							content().contentType(MediaType.APPLICATION_JSON),
							jsonPath("$.employee").value("alice"),
							jsonPath("$.status").value("PENDING"));
		}

		@Test
		void testViewRequest() throws Exception {
			LeaveRequest saved = repository
					.save(new LeaveRequest("alice", of(2022, 11, 30), of(2022, 12, 3), APPROVED));
			mockmvc.perform(get("/view/request/{id}", saved.getId()))
					.andExpectAll(
							status().isOk(),
							content().contentType(MediaType.APPLICATION_JSON),
							jsonPath("$.employee").value("alice"),
							jsonPath("$.status").value("APPROVED"));
		}

		@Test
		void testViewEmployee() throws Exception {
			repository.save(new LeaveRequest("alice", of(2022, 11, 30), of(2022, 12, 3), APPROVED));
			mockmvc.perform(get("/view/employee/{employee}", "alice"))
					.andExpectAll(
							status().isOk(),
							content().contentType(MediaType.APPLICATION_JSON),
							jsonPath("$[0].employee").value("alice"),
							jsonPath("$[0].status").value("APPROVED"));
		}

	}

	@Nested
	class AuthorizeRole {

		// TODO Authenticate with HR role when making these requests

		@Test
		void testApprove() throws Exception {
			LeaveRequest saved = repository
					.save(new LeaveRequest("alice", of(2022, 11, 30), of(2022, 12, 3), PENDING));
			mockmvc.perform(post("/approve/{id}", saved.getId()))
					.andExpectAll(
							status().isAccepted(),
							content().contentType(MediaType.APPLICATION_JSON),
							jsonPath("$.employee").value("alice"),
							jsonPath("$.status").value("APPROVED"));
		}

		@Test
		void testApproveMissing() throws Exception {
			mockmvc.perform(post("/approve/{id}", UUID.randomUUID()))
					.andExpect(status().isNoContent());
		}

		@Test
		void testDeny() throws Exception {
			LeaveRequest saved = repository.save(new LeaveRequest("alice", of(2022, 11, 30), of(2022, 12, 3), PENDING));
			mockmvc.perform(post("/deny/{id}", saved.getId()))
					.andExpectAll(
							status().isAccepted(),
							content().contentType(MediaType.APPLICATION_JSON),
							jsonPath("$.employee").value("alice"),
							jsonPath("$.status").value("DENIED"));
		}

		@Test
		void testViewRequestMissing() throws Exception {
			mockmvc.perform(get("/view/request/{id}", UUID.randomUUID()))
					.andExpect(status().isNoContent());
		}

		@Test
		void testViewAll() throws Exception {
			repository.save(new LeaveRequest("alice", of(2022, 11, 30), of(2022, 12, 3), APPROVED));
			mockmvc.perform(get("/view/all"))
					.andExpectAll(
							status().isOk(),
							content().contentType(MediaType.APPLICATION_JSON),
							jsonPath("$[0].employee").value("alice"),
							jsonPath("$[0].status").value("APPROVED"));
		}
	}
}
