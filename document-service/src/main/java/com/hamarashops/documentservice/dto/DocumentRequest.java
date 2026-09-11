package com.hamarashops.documentservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class DocumentRequest {

	@NotNull(message = "Candidate ID is required")
	private Integer candidateId;

	@NotBlank(message = "Document name is required")
	private String documentName;

	private String documentType;

	@NotBlank(message = "File name is required")
	private String fileName;

	private String filePath;

	private String fileUrl;

	private Long fileSize;

	private String contentType;

	public DocumentRequest() {
		super();
	}

	public DocumentRequest(Integer candidateId, String documentName, String documentType, String fileName,
			String filePath, String fileUrl, Long fileSize, String contentType) {
		super();
		this.candidateId = candidateId;
		this.documentName = documentName;
		this.documentType = documentType;
		this.fileName = fileName;
		this.filePath = filePath;
		this.fileUrl = fileUrl;
		this.fileSize = fileSize;
		this.contentType = contentType;
	}

	public Integer getCandidateId() {
		return candidateId;
	}

	public void setCandidateId(Integer candidateId) {
		this.candidateId = candidateId;
	}

	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
}
