package com.hamarashops.documentservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hamarashops.documentservice.dto.DocumentRequest;
import com.hamarashops.documentservice.dto.DocumentResponse;
import com.hamarashops.documentservice.exception.ResourceNotFoundException;
import com.hamarashops.documentservice.model.Document;
import com.hamarashops.documentservice.repo.DocumentRepo;
import com.hamarashops.documentservice.service.impl.DocumentServiceImpl;
import com.hamarashops.documentservice.client.AuditServiceClient;

@ExtendWith(MockitoExtension.class)
class DocumentServiceTest {

	@Mock
	private DocumentRepo documentRepo;

	@Mock
	private AuditServiceClient auditServiceClient;

	@InjectMocks
	private DocumentServiceImpl documentService;

	private Document document;
	private DocumentRequest request;

	@BeforeEach
	void setUp() {
		document = new Document();
		document.setId(1);
		document.setCandidateId(101);
		document.setDocumentName("Resume");
		document.setDocumentType("PDF");
		document.setFileName("john_resume.pdf");
		document.setFilePath("C:/CandidateResumes/john_resume.pdf");
		document.setFileUrl("https://storage.googleapis.com/documents/john_resume.pdf");
		document.setFileSize(2048576L);
		document.setContentType("application/pdf");

		request = new DocumentRequest();
		request.setCandidateId(101);
		request.setDocumentName("Resume");
		request.setDocumentType("PDF");
		request.setFileName("john_resume.pdf");
		request.setFilePath("C:/CandidateResumes/john_resume.pdf");
		request.setFileUrl("https://storage.googleapis.com/documents/john_resume.pdf");
		request.setFileSize(2048576L);
		request.setContentType("application/pdf");
	}

	@Test
	void testCreateDocument_Success() {
		when(documentRepo.save(any(Document.class))).thenReturn(document);

		DocumentResponse response = documentService.createDocument(request);

		assertNotNull(response);
		assertEquals(1, response.getId());
		assertEquals("Resume", response.getDocumentName());
		assertEquals("https://storage.googleapis.com/documents/john_resume.pdf", response.getFileUrl());
		verify(documentRepo).save(any(Document.class));
		verify(auditServiceClient).logAudit(any(), any(), any(), any(), any(), any());
	}

	@Test
	void testGetDocumentById_Success() {
		when(documentRepo.findById(1)).thenReturn(Optional.of(document));

		DocumentResponse response = documentService.getDocumentById(1);

		assertNotNull(response);
		assertEquals(1, response.getId());
		assertEquals("john_resume.pdf", response.getFileName());
	}

	@Test
	void testGetDocumentById_NotFoundThrowsException() {
		when(documentRepo.findById(99)).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> documentService.getDocumentById(99));
	}

	@Test
	void testGetAllDocuments_Success() {
		when(documentRepo.findAll()).thenReturn(List.of(document));

		List<DocumentResponse> list = documentService.getAllDocuments();

		assertEquals(1, list.size());
		assertEquals("Resume", list.get(0).getDocumentName());
	}
}
