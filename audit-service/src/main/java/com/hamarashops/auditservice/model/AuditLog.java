package com.hamarashops.auditservice.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "audit_logs")
public class AuditLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "user_id")
	private Integer userId;

	@Column(name = "service_name", nullable = false)
	private String serviceName;

	@Column(nullable = false)
	private String action;

	@Column(name = "entity_name")
	private String entityName;

	@Column(name = "entity_id")
	private String entityId;

	@Column(length = 1000)
	private String description;

	@Column(name = "ip_address")
	private String ipAddress;

	@Column
	private String status;

	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	public AuditLog() {
		super();
	}

	public AuditLog(Integer id, Integer userId, String serviceName, String action, String entityName, String entityId,
			String description, String ipAddress, String status) {
		super();
		this.id = id;
		this.userId = userId;
		this.serviceName = serviceName;
		this.action = action;
		this.entityName = entityName;
		this.entityId = entityId;
		this.description = description;
		this.ipAddress = ipAddress;
		this.status = status;
	}

	@PrePersist
	protected void onCreate() {
		this.createdAt = LocalDateTime.now();
		if (this.status == null) {
			this.status = "SUCCESS";
		}
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
}
