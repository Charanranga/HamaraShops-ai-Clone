package com.hamarashops.documentservice.controller;

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

import com.hamarashops.documentservice.dto.DocumentRequest;
import com.hamarashops.documentservice.dto.DocumentResponse;
import com.hamarashops.documentservice.service.DocumentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/documents")
@Tag(name = "Document Management API", description = "REST APIs for Managing Candidate Documents")
public class DocumentController {

	private static final Logger logger = LoggerFactory.getLogger(DocumentController.class);

	@Autowired
	private DocumentService documentService;

	@PostMapping
	@Operation(summary = "Create Document", description = "Create a new candidate document record")
	public ResponseEntity<DocumentResponse> createDocument(@Valid @RequestBody DocumentRequest request) {
		logger.info("REST POST /documents invoked for document: {}", request.getDocumentName());
		DocumentResponse response = documentService.createDocument(request);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@GetMapping
	@Operation(summary = "Get All Documents", description = "Retrieve list of all candidate documents")
	public ResponseEntity<List<DocumentResponse>> getAllDocuments() {
		logger.info("REST GET /documents invoked");
		List<DocumentResponse> responseList = documentService.getAllDocuments();
		return ResponseEntity.ok(responseList);
	}

	@GetMapping("/{id}")
	@Operation(summary = "Get Document by ID", description = "Retrieve document details by document ID")
	public ResponseEntity<DocumentResponse> getDocumentById(@PathVariable Integer id) {
		logger.info("REST GET /documents/{} invoked", id);
		DocumentResponse response = documentService.getDocumentById(id);
		return ResponseEntity.ok(response);
	}

	@PutMapping("/{id}")
	@Operation(summary = "Update Document", description = "Update document details by ID")
	public ResponseEntity<DocumentResponse> updateDocument(@PathVariable Integer id,
			@Valid @RequestBody DocumentRequest request) {
		logger.info("REST PUT /documents/{} invoked", id);
		DocumentResponse response = documentService.updateDocument(id, request);
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Delete Document", description = "Delete document record by ID")
	public ResponseEntity<Void> deleteDocument(@PathVariable Integer id) {
		logger.info("REST DELETE /documents/{} invoked", id);
		documentService.deleteDocument(id);
		return ResponseEntity.noContent().build();
	}

	@PostMapping(value = "/upload", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(summary = "Upload Document File", description = "Upload a candidate's resume file and save its metadata")
	public ResponseEntity<DocumentResponse> uploadDocument(
			@org.springframework.web.bind.annotation.RequestParam("file") org.springframework.web.multipart.MultipartFile file,
			@org.springframework.web.bind.annotation.RequestParam("candidateId") Integer candidateId) {
		logger.info("REST POST /documents/upload invoked for candidate: {}", candidateId);
		DocumentResponse response = documentService.uploadDocumentFile(file, candidateId);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@GetMapping("/view/{fileName:.+}")
	@Operation(summary = "View Document File", description = "Download/serve a locally uploaded document file")
	public ResponseEntity<org.springframework.core.io.Resource> viewDocumentFile(
			@PathVariable String fileName,
			jakarta.servlet.http.HttpServletRequest request) {
		logger.info("REST GET /documents/view/{} invoked", fileName);
		return documentService.loadLocalFile(fileName, request);
	}
}
