package com.hamarashops.auditservice.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hamarashops.auditservice.dto.AuditLogRequest;
import com.hamarashops.auditservice.dto.AuditLogResponse;
import com.hamarashops.auditservice.exception.ResourceNotFoundException;
import com.hamarashops.auditservice.model.AuditLog;
import com.hamarashops.auditservice.repo.AuditLogRepo;
import com.hamarashops.auditservice.service.AuditLogService;

@Service
@Transactional
public class AuditLogServiceImpl implements AuditLogService {

	private static final Logger logger = LoggerFactory.getLogger(AuditLogServiceImpl.class);

	@Autowired
	private AuditLogRepo auditLogRepo;

	@Override
	public AuditLogResponse createAuditLog(AuditLogRequest request) {
		logger.info("Creating audit log: action '{}' for service '{}'", request.getAction(), request.getServiceName());

		AuditLog auditLog = new AuditLog();
		auditLog.setUserId(request.getUserId());
		auditLog.setServiceName(request.getServiceName());
		auditLog.setAction(request.getAction());
		auditLog.setEntityName(request.getEntityName());
		auditLog.setEntityId(request.getEntityId());
		auditLog.setDescription(request.getDescription());
		auditLog.setIpAddress(request.getIpAddress());
		auditLog.setStatus(request.getStatus());

		AuditLog saved = auditLogRepo.save(auditLog);
		logger.info("Successfully created audit log ID: {}", saved.getId());
		return mapToResponse(saved);
	}

	@Override
	@Transactional(readOnly = true)
	public List<AuditLogResponse> getAllAuditLogs() {
		logger.info("Fetching all audit logs");
		return auditLogRepo.findAll().stream()
				.map(this::mapToResponse)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public AuditLogResponse getAuditLogById(Integer id) {
		logger.info("Fetching audit log by ID: {}", id);
		AuditLog auditLog = auditLogRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Audit log not found with ID: " + id));
		return mapToResponse(auditLog);
	}

	@Override
	public void deleteAuditLog(Integer id) {
		logger.info("Deleting audit log ID: {}", id);
		AuditLog auditLog = auditLogRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Audit log not found with ID: " + id));
		auditLogRepo.delete(auditLog);
		logger.info("Successfully deleted audit log ID: {}", id);
	}

	private AuditLogResponse mapToResponse(AuditLog auditLog) {
		AuditLogResponse response = new AuditLogResponse();
		response.setId(auditLog.getId());
		response.setUserId(auditLog.getUserId());
		response.setServiceName(auditLog.getServiceName());
		response.setAction(auditLog.getAction());
		response.setEntityName(auditLog.getEntityName());
		response.setEntityId(auditLog.getEntityId());
		response.setDescription(auditLog.getDescription());
		response.setIpAddress(auditLog.getIpAddress());
		response.setStatus(auditLog.getStatus());
		response.setCreatedAt(auditLog.getCreatedAt());
		return response;
	}
}
