package com.hamarashops.documentservice.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hamarashops.documentservice.model.Document;

@Repository
public interface DocumentRepo extends JpaRepository<Document, Integer> {

	List<Document> findByCandidateId(Integer candidateId);
}
