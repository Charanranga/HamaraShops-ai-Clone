package com.hamarashops.documentservice.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hamarashops.documentservice.dto.DocumentRequest;
import com.hamarashops.documentservice.dto.DocumentResponse;
import com.hamarashops.documentservice.exception.ResourceNotFoundException;
import com.hamarashops.documentservice.model.Document;
import com.hamarashops.documentservice.repo.DocumentRepo;
import com.hamarashops.documentservice.service.DocumentService;
import com.hamarashops.documentservice.client.AuditServiceClient;

import org.springframework.beans.factory.annotation.Value;
import com.hamarashops.documentservice.service.StorageService;

@Service
@Transactional
public class DocumentServiceImpl implements DocumentService {

	private static final Logger logger = LoggerFactory.getLogger(DocumentServiceImpl.class);

	@Autowired
	private DocumentRepo documentRepo;

	@Autowired
	private AuditServiceClient auditServiceClient;

	@Autowired
	private StorageService storageService;

	@Value("${file.upload-dir:C:/CandidateResumes}")
	private String localUploadDir;

	@Value("${document.download-base-url:http://localhost:8080/api/documents/view}")
	private String downloadBaseUrl;

	@Override
	public DocumentResponse createDocument(DocumentRequest request) {
		logger.info("Creating document record: {} for candidate ID: {}", request.getDocumentName(), request.getCandidateId());

		Document document = new Document();
		document.setCandidateId(request.getCandidateId());
		document.setDocumentName(request.getDocumentName());
		document.setDocumentType(request.getDocumentType());
		document.setFileName(request.getFileName());
		document.setFilePath(request.getFilePath());
		document.setFileUrl(request.getFileUrl());
		document.setFileSize(request.getFileSize());
		document.setContentType(request.getContentType());

		Document saved = documentRepo.save(document);
		logger.info("Successfully created document ID: {}", saved.getId());

		try {
			auditServiceClient.logAudit(
					saved.getCandidateId(),
					"Upload Document",
					"Document",
					String.valueOf(saved.getId()),
					"Successfully uploaded document: " + saved.getDocumentName(),
					"SUCCESS"
			);
		} catch (Exception e) {
			logger.warn("Failed to log audit event: {}", e.getMessage());
		}

		return mapToResponse(saved);
	}

	@Override
	@Transactional(readOnly = true)
	public List<DocumentResponse> getAllDocuments() {
		logger.info("Fetching all documents");
		return documentRepo.findAll().stream()
				.map(this::mapToResponse)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public DocumentResponse getDocumentById(Integer id) {
		logger.info("Fetching document by ID: {}", id);
		Document document = documentRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Document not found with ID: " + id));
		return mapToResponse(document);
	}

	@Override
	public DocumentResponse updateDocument(Integer id, DocumentRequest request) {
		logger.info("Updating document ID: {}", id);
		Document document = documentRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Document not found with ID: " + id));

		document.setCandidateId(request.getCandidateId());
		document.setDocumentName(request.getDocumentName());
		document.setDocumentType(request.getDocumentType());
		document.setFileName(request.getFileName());
		document.setFilePath(request.getFilePath());
		document.setFileUrl(request.getFileUrl());
		document.setFileSize(request.getFileSize());
		document.setContentType(request.getContentType());

		Document updated = documentRepo.save(document);
		logger.info("Successfully updated document ID: {}", updated.getId());

		try {
			auditServiceClient.logAudit(
					updated.getCandidateId(),
					"Update Document",
					"Document",
					String.valueOf(updated.getId()),
					"Successfully updated document: " + updated.getDocumentName(),
					"SUCCESS"
			);
		} catch (Exception e) {
			logger.warn("Failed to log audit event: {}", e.getMessage());
		}

		return mapToResponse(updated);
	}

	@Override
	public void deleteDocument(Integer id) {
		logger.info("Deleting document ID: {}", id);
		Document document = documentRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Document not found with ID: " + id));
		documentRepo.delete(document);
		logger.info("Successfully deleted document ID: {}", id);

		try {
			auditServiceClient.logAudit(
					document.getCandidateId(),
					"Delete Document",
					"Document",
					String.valueOf(document.getId()),
					"Successfully deleted document ID: " + document.getId(),
					"SUCCESS"
			);
		} catch (Exception e) {
			logger.warn("Failed to log audit event: {}", e.getMessage());
		}
	}

	@Override
	public DocumentResponse uploadDocumentFile(org.springframework.web.multipart.MultipartFile file, Integer candidateId) {
		try {
			String result = storageService.store(file);
			
			Document document = new Document();
			document.setCandidateId(candidateId);
			document.setDocumentName("Resume");
			document.setDocumentType(file.getContentType());
			document.setFileName(file.getOriginalFilename());
			document.setFileSize(file.getSize());
			document.setContentType(file.getContentType());
			
			if (result.startsWith("http")) {
				document.setFileUrl(result);
				document.setFilePath(result);
			} else {
				document.setFilePath(localUploadDir + "/" + result);
				document.setFileUrl(downloadBaseUrl + "/" + result);
			}
			
			Document saved = documentRepo.save(document);
			logger.info("Successfully uploaded file. Saved ID: {}", saved.getId());
			
			try {
				auditServiceClient.logAudit(
						saved.getCandidateId(),
						"Upload Document",
						"Document",
						String.valueOf(saved.getId()),
						"Successfully uploaded document: " + saved.getFileName(),
						"SUCCESS"
				);
			} catch (Exception e) {
				logger.warn("Failed to log audit event: {}", e.getMessage());
			}
			
			return mapToResponse(saved);
		} catch (java.io.IOException e) {
			logger.error("Failed to store file: {}", e.getMessage());
			throw new RuntimeException("Failed to store file: " + e.getMessage());
		}
	}

	@Override
	public org.springframework.http.ResponseEntity<org.springframework.core.io.Resource> loadLocalFile(
			String fileName, jakarta.servlet.http.HttpServletRequest request) {
		try {
			java.nio.file.Path filePath = java.nio.file.Paths.get(localUploadDir).resolve(fileName).normalize();
			org.springframework.core.io.Resource resource = new org.springframework.core.io.UrlResource(filePath.toUri());
			if (resource.exists()) {
				String contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
				if (contentType == null) {
					contentType = "application/octet-stream";
				}
				return org.springframework.http.ResponseEntity.ok()
						.contentType(org.springframework.http.MediaType.parseMediaType(contentType))
						.header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
						.body(resource);
			} else {
				return org.springframework.http.ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			logger.error("Could not load local file: {}", e.getMessage());
			return org.springframework.http.ResponseEntity.internalServerError().build();
		}
	}

	private DocumentResponse mapToResponse(Document document) {
		DocumentResponse response = new DocumentResponse();
		response.setId(document.getId());
		response.setCandidateId(document.getCandidateId());
		response.setDocumentName(document.getDocumentName());
		response.setDocumentType(document.getDocumentType());
		response.setFileName(document.getFileName());
		response.setFilePath(document.getFilePath());
		response.setFileUrl(document.getFileUrl());
		response.setFileSize(document.getFileSize());
		response.setContentType(document.getContentType());
		response.setUploadedAt(document.getUploadedAt());
		response.setUpdatedAt(document.getUpdatedAt());
		return response;
	}
}
