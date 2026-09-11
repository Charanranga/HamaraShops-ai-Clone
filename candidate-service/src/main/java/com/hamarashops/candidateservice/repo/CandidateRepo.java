package com.hamarashops.candidateservice.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hamarashops.candidateservice.model.Candidate;

@Repository
public interface CandidateRepo extends JpaRepository<Candidate, Integer> {

	Optional<Candidate> findByEmail(String email);

	boolean existsByEmail(String email);
}
