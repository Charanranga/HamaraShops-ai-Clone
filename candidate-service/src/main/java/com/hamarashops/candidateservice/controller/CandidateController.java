package com.hamarashops.candidateservice.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hamarashops.candidateservice.dto.CandidateRequest;
import com.hamarashops.candidateservice.dto.CandidateResponse;
import com.hamarashops.candidateservice.service.CandidateService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/candidates")
@Tag(name = "Candidate Management API", description = "REST APIs for Managing Candidate Profiles")
public class CandidateController {

	private static final Logger logger = LoggerFactory.getLogger(CandidateController.class);

	@Autowired
	private CandidateService candidateService;

	@PostMapping
	@Operation(summary = "Create Candidate", description = "Create a new candidate profile record")
	public ResponseEntity<CandidateResponse> createCandidate(@Valid @RequestBody CandidateRequest request) {
		logger.info("REST POST /candidates invoked for email: {}", request.getEmail());
		CandidateResponse response = candidateService.createCandidate(request);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@GetMapping
	@Operation(summary = "Get All Candidates", description = "Retrieve list of all candidate profiles")
	public ResponseEntity<List<CandidateResponse>> getAllCandidates() {
		logger.info("REST GET /candidates invoked");
		List<CandidateResponse> responseList = candidateService.getAllCandidates();
		return ResponseEntity.ok(responseList);
	}

	@GetMapping("/{id}")
	@Operation(summary = "Get Candidate by ID", description = "Retrieve candidate profile details by candidate ID")
	public ResponseEntity<CandidateResponse> getCandidateById(@PathVariable Integer id) {
		logger.info("REST GET /candidates/{} invoked", id);
		CandidateResponse response = candidateService.getCandidateById(id);
		return ResponseEntity.ok(response);
	}

	@PutMapping("/{id}")
	@Operation(summary = "Update Candidate", description = "Update candidate profile details by ID")
	public ResponseEntity<CandidateResponse> updateCandidate(@PathVariable Integer id,
			@Valid @RequestBody CandidateRequest request) {
		logger.info("REST PUT /candidates/{} invoked", id);
		CandidateResponse response = candidateService.updateCandidate(id, request);
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Delete Candidate", description = "Delete candidate profile by ID")
	public ResponseEntity<Void> deleteCandidate(@PathVariable Integer id) {
		logger.info("REST DELETE /candidates/{} invoked", id);
		candidateService.deleteCandidate(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/user/{userId}")
	@Operation(summary = "Get Candidate by User ID", description = "Retrieve candidate profile by user ID")
	public ResponseEntity<CandidateResponse> getCandidateByUserId(@PathVariable Integer userId) {
		logger.info("REST GET /candidates/user/{} invoked", userId);
		CandidateResponse response = candidateService.getCandidateByUserId(userId);
		return ResponseEntity.ok(response);
	}
}
