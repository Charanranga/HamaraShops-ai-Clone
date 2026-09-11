package com.hamarashops.candidateservice.dto;

public class AuditLogRequest {

	private Integer userId;
	private String serviceName;
	private String action;
	private String entityName;
	private String entityId;
	private String description;
	private String ipAddress;
	private String status;

	public AuditLogRequest() {
		super();
	}

	public AuditLogRequest(Integer userId, String serviceName, String action, String entityName, String entityId,
			String description, String ipAddress, String status) {
		super();
		this.userId = userId;
		this.serviceName = serviceName;
		this.action = action;
		this.entityName = entityName;
		this.entityId = entityId;
		this.description = description;
		this.ipAddress = ipAddress;
		this.status = status;
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
}
