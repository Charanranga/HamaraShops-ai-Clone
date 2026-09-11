package com.hamarashops.candidateservice.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hamarashops.candidateservice.dto.CandidateRequest;
import com.hamarashops.candidateservice.dto.CandidateResponse;
import com.hamarashops.candidateservice.exception.DuplicateCandidateException;
import com.hamarashops.candidateservice.exception.ResourceNotFoundException;
import com.hamarashops.candidateservice.model.Candidate;
import com.hamarashops.candidateservice.repo.CandidateRepo;
import com.hamarashops.candidateservice.service.CandidateService;
import com.hamarashops.candidateservice.client.AuditServiceClient;

@Service
@Transactional
public class CandidateServiceImpl implements CandidateService {

	private static final Logger logger = LoggerFactory.getLogger(CandidateServiceImpl.class);

	@Autowired
	private CandidateRepo candidateRepo;

	@Autowired
	private AuditServiceClient auditServiceClient;

	@Override
	public CandidateResponse createCandidate(CandidateRequest request) {
		logger.info("Creating candidate with email: {}", request.getEmail());
		if (candidateRepo.existsByEmail(request.getEmail())) {
			throw new DuplicateCandidateException("Candidate with email '" + request.getEmail() + "' already exists!");
		}

		Candidate candidate = new Candidate();
		candidate.setUserId(request.getUserId());
		candidate.setFirstName(request.getFirstName());
		candidate.setLastName(request.getLastName());
		candidate.setEmail(request.getEmail());
		candidate.setPhone(request.getPhone());
		candidate.setDateOfBirth(request.getDateOfBirth());
		candidate.setGender(request.getGender());
		candidate.setQualification(request.getQualification());
		candidate.setExperience(request.getExperience());
		candidate.setSkills(request.getSkills());
		candidate.setResumeUrl(request.getResumeUrl());
		candidate.setStatus(request.getStatus());

		Candidate saved = candidateRepo.save(candidate);
		logger.info("Successfully created candidate ID: {}", saved.getId());

		try {
			auditServiceClient.logAudit(
					saved.getUserId(),
					"Create Candidate",
					"Candidate",
					String.valueOf(saved.getId()),
					"Successfully created candidate: " + saved.getEmail(),
					"SUCCESS"
			);
		} catch (Exception e) {
			logger.warn("Failed to log audit event: {}", e.getMessage());
		}

		return mapToResponse(saved);
	}

	@Override
	@Transactional(readOnly = true)
	public List<CandidateResponse> getAllCandidates() {
		logger.info("Fetching all candidates");
		return candidateRepo.findAll().stream()
				.map(this::mapToResponse)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public CandidateResponse getCandidateById(Integer id) {
		logger.info("Fetching candidate by ID: {}", id);
		Candidate candidate = candidateRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Candidate not found with ID: " + id));
		return mapToResponse(candidate);
	}

	@Override
	public CandidateResponse updateCandidate(Integer id, CandidateRequest request) {
		logger.info("Updating candidate ID: {}", id);
		Candidate candidate = candidateRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Candidate not found with ID: " + id));

		if (!candidate.getEmail().equalsIgnoreCase(request.getEmail()) && candidateRepo.existsByEmail(request.getEmail())) {
			throw new DuplicateCandidateException("Candidate with email '" + request.getEmail() + "' already exists!");
		}

		candidate.setUserId(request.getUserId());
		candidate.setFirstName(request.getFirstName());
		candidate.setLastName(request.getLastName());
		candidate.setEmail(request.getEmail());
		candidate.setPhone(request.getPhone());
		candidate.setDateOfBirth(request.getDateOfBirth());
		candidate.setGender(request.getGender());
		candidate.setQualification(request.getQualification());
		candidate.setExperience(request.getExperience());
		candidate.setSkills(request.getSkills());
		candidate.setResumeUrl(request.getResumeUrl());
		if (request.getStatus() != null) {
			candidate.setStatus(request.getStatus());
		}

		Candidate updated = candidateRepo.save(candidate);
		logger.info("Successfully updated candidate ID: {}", updated.getId());

		try {
			auditServiceClient.logAudit(
					updated.getUserId(),
					"Update Candidate",
					"Candidate",
					String.valueOf(updated.getId()),
					"Successfully updated candidate: " + updated.getEmail(),
					"SUCCESS"
			);
		} catch (Exception e) {
			logger.warn("Failed to log audit event: {}", e.getMessage());
		}

		return mapToResponse(updated);
	}

	@Override
	public void deleteCandidate(Integer id) {
		logger.info("Deleting candidate ID: {}", id);
		Candidate candidate = candidateRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Candidate not found with ID: " + id));
		candidateRepo.delete(candidate);
		logger.info("Successfully deleted candidate ID: {}", id);

		try {
			auditServiceClient.logAudit(
					candidate.getUserId(),
					"Delete Candidate",
					"Candidate",
					String.valueOf(candidate.getId()),
					"Successfully deleted candidate ID: " + candidate.getId(),
					"SUCCESS"
			);
		} catch (Exception e) {
			logger.warn("Failed to log audit event: {}", e.getMessage());
		}
	}

	@Override
	@Transactional(readOnly = true)
	public CandidateResponse getCandidateByUserId(Integer userId) {
		logger.info("Fetching candidate by User ID: {}", userId);
		Candidate candidate = candidateRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("Candidate not found with User ID: " + userId));
		return mapToResponse(candidate);
	}

	private CandidateResponse mapToResponse(Candidate candidate) {
		CandidateResponse response = new CandidateResponse();
		response.setId(candidate.getId());
		response.setUserId(candidate.getUserId());
		response.setFirstName(candidate.getFirstName());
		response.setLastName(candidate.getLastName());
		response.setEmail(candidate.getEmail());
		response.setPhone(candidate.getPhone());
		response.setDateOfBirth(candidate.getDateOfBirth());
		response.setGender(candidate.getGender());
		response.setQualification(candidate.getQualification());
		response.setExperience(candidate.getExperience());
		response.setSkills(candidate.getSkills());
		response.setResumeUrl(candidate.getResumeUrl());
		response.setStatus(candidate.getStatus());
		response.setCreatedAt(candidate.getCreatedAt());
		response.setUpdatedAt(candidate.getUpdatedAt());
		return response;
	}
}
