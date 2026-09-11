package com.hamarashops.documentservice.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "documents")
public class Document {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "candidate_id", nullable = false)
	private Integer candidateId;

	@Column(name = "document_name", nullable = false)
	private String documentName;

	@Column(name = "document_type")
	private String documentType;

	@Column(name = "file_name", nullable = false)
	private String fileName;

	@Column(name = "file_path")
	private String filePath;

	@Column(name = "file_url")
	private String fileUrl;

	@Column(name = "file_size")
	private Long fileSize;

	@Column(name = "content_type")
	private String contentType;

	@Column(name = "uploaded_at", updatable = false)
	private LocalDateTime uploadedAt;

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	public Document() {
		super();
	}

	public Document(Integer id, Integer candidateId, String documentName, String documentType, String fileName,
			String filePath, String fileUrl, Long fileSize, String contentType) {
		super();
		this.id = id;
		this.candidateId = candidateId;
		this.documentName = documentName;
		this.documentType = documentType;
		this.fileName = fileName;
		this.filePath = filePath;
		this.fileUrl = fileUrl;
		this.fileSize = fileSize;
		this.contentType = contentType;
	}

	@PrePersist
	protected void onCreate() {
		this.uploadedAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();
	}

	@PreUpdate
	protected void onUpdate() {
		this.updatedAt = LocalDateTime.now();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public LocalDateTime getUploadedAt() {
		return uploadedAt;
	}

	public void setUploadedAt(LocalDateTime uploadedAt) {
		this.uploadedAt = uploadedAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
}
