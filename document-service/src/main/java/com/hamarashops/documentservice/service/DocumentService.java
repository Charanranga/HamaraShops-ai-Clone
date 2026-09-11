package com.hamarashops.documentservice.service;

import java.util.List;

import com.hamarashops.documentservice.dto.DocumentRequest;
import com.hamarashops.documentservice.dto.DocumentResponse;

public interface DocumentService {

	DocumentResponse createDocument(DocumentRequest request);

	List<DocumentResponse> getAllDocuments();

	DocumentResponse getDocumentById(Integer id);

	DocumentResponse updateDocument(Integer id, DocumentRequest request);

	void deleteDocument(Integer id);

	DocumentResponse uploadDocumentFile(org.springframework.web.multipart.MultipartFile file, Integer candidateId);

	org.springframework.http.ResponseEntity<org.springframework.core.io.Resource> loadLocalFile(String fileName, jakarta.servlet.http.HttpServletRequest request);
}
