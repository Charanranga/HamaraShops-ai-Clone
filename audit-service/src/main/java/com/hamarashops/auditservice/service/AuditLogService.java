package com.hamarashops.auditservice.service;

import java.util.List;

import com.hamarashops.auditservice.dto.AuditLogRequest;
import com.hamarashops.auditservice.dto.AuditLogResponse;

public interface AuditLogService {

	AuditLogResponse createAuditLog(AuditLogRequest request);

	List<AuditLogResponse> getAllAuditLogs();

	AuditLogResponse getAuditLogById(Integer id);

	void deleteAuditLog(Integer id);
}
