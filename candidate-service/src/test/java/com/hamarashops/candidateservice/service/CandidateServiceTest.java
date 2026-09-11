package com.hamarashops.candidateservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hamarashops.candidateservice.dto.CandidateRequest;
import com.hamarashops.candidateservice.dto.CandidateResponse;
import com.hamarashops.candidateservice.exception.DuplicateCandidateException;
import com.hamarashops.candidateservice.exception.ResourceNotFoundException;
import com.hamarashops.candidateservice.model.Candidate;
import com.hamarashops.candidateservice.repo.CandidateRepo;
import com.hamarashops.candidateservice.service.impl.CandidateServiceImpl;
import com.hamarashops.candidateservice.client.AuditServiceClient;

@ExtendWith(MockitoExtension.class)
class CandidateServiceTest {

	@Mock
	private CandidateRepo candidateRepo;

	@Mock
	private AuditServiceClient auditServiceClient;

	@InjectMocks
	private CandidateServiceImpl candidateService;

	private Candidate candidate;
	private CandidateRequest request;

	@BeforeEach
	void setUp() {
		candidate = new Candidate();
		candidate.setId(1);
		candidate.setUserId(101);
		candidate.setFirstName("John");
		candidate.setLastName("Doe");
		candidate.setEmail("john.doe@example.com");
		candidate.setPhone("9876543210");
		candidate.setDateOfBirth(LocalDate.of(1995, 5, 20));
		candidate.setGender("Male");
		candidate.setQualification("B.Tech CSE");
		candidate.setExperience(3);
		candidate.setSkills("Java, Spring Boot, MySQL");
		candidate.setResumeUrl("https://storage.googleapis.com/resumes/john_doe.pdf");
		candidate.setStatus("APPLIED");

		request = new CandidateRequest();
		request.setUserId(101);
		request.setFirstName("John");
		request.setLastName("Doe");
		request.setEmail("john.doe@example.com");
		request.setPhone("9876543210");
		request.setDateOfBirth(LocalDate.of(1995, 5, 20));
		request.setGender("Male");
		request.setQualification("B.Tech CSE");
		request.setExperience(3);
		request.setSkills("Java, Spring Boot, MySQL");
		request.setResumeUrl("https://storage.googleapis.com/resumes/john_doe.pdf");
		request.setStatus("APPLIED");
	}

	@Test
	void testCreateCandidate_Success() {
		when(candidateRepo.existsByEmail(request.getEmail())).thenReturn(false);
		when(candidateRepo.save(any(Candidate.class))).thenReturn(candidate);

		CandidateResponse response = candidateService.createCandidate(request);

		assertNotNull(response);
		assertEquals(1, response.getId());
		assertEquals("john.doe@example.com", response.getEmail());
		assertEquals(3, response.getExperience());
		verify(candidateRepo).save(any(Candidate.class));
		verify(auditServiceClient).logAudit(any(), any(), any(), any(), any(), any());
	}

	@Test
	void testCreateCandidate_DuplicateEmailThrowsException() {
		when(candidateRepo.existsByEmail(request.getEmail())).thenReturn(true);

		assertThrows(DuplicateCandidateException.class, () -> candidateService.createCandidate(request));
	}

	@Test
	void testGetCandidateById_Success() {
		when(candidateRepo.findById(1)).thenReturn(Optional.of(candidate));

		CandidateResponse response = candidateService.getCandidateById(1);

		assertNotNull(response);
		assertEquals(1, response.getId());
		assertEquals("John", response.getFirstName());
	}

	@Test
	void testGetCandidateById_NotFoundThrowsException() {
		when(candidateRepo.findById(99)).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> candidateService.getCandidateById(99));
	}

	@Test
	void testGetAllCandidates_Success() {
		when(candidateRepo.findAll()).thenReturn(List.of(candidate));

		List<CandidateResponse> list = candidateService.getAllCandidates();

		assertEquals(1, list.size());
		assertEquals("john.doe@example.com", list.get(0).getEmail());
	}
}
