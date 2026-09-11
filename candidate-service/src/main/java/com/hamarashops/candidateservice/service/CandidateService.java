package com.hamarashops.candidateservice.service;

import java.util.List;

import com.hamarashops.candidateservice.dto.CandidateRequest;
import com.hamarashops.candidateservice.dto.CandidateResponse;

public interface CandidateService {

	CandidateResponse createCandidate(CandidateRequest request);

	List<CandidateResponse> getAllCandidates();

	CandidateResponse getCandidateById(Integer id);

	CandidateResponse updateCandidate(Integer id, CandidateRequest request);

	void deleteCandidate(Integer id);

	CandidateResponse getCandidateByUserId(Integer userId);
}
