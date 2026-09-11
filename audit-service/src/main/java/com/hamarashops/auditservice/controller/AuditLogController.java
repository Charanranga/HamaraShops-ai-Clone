package com.hamarashops.auditservice.controller;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hamarashops.auditservice.dto.AuditLogRequest;
import com.hamarashops.auditservice.dto.AuditLogResponse;
import com.hamarashops.auditservice.service.AuditLogService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/audit")
@Tag(name = "Audit Log Management API", description = "REST APIs for Managing System Audit Logs")
public class AuditLogController {

	private static final Logger logger = LoggerFactory.getLogger(AuditLogController.class);

	@Autowired
	private AuditLogService auditLogService;

	@PostMapping
	@Operation(summary = "Create Audit Log", description = "Create a new system audit log record")
	public ResponseEntity<AuditLogResponse> createAuditLog(@Valid @RequestBody AuditLogRequest request) {
		logger.info("REST POST /audit invoked for service: {}", request.getServiceName());
		AuditLogResponse response = auditLogService.createAuditLog(request);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@GetMapping
	@Operation(summary = "Get All Audit Logs", description = "Retrieve list of all system audit logs")
	public ResponseEntity<List<AuditLogResponse>> getAllAuditLogs() {
		logger.info("REST GET /audit invoked");
		List<AuditLogResponse> responseList = auditLogService.getAllAuditLogs();
		return ResponseEntity.ok(responseList);
	}

	@GetMapping("/{id}")
	@Operation(summary = "Get Audit Log by ID", description = "Retrieve audit log details by audit log ID")
	public ResponseEntity<AuditLogResponse> getAuditLogById(@PathVariable Integer id) {
		logger.info("REST GET /audit/{} invoked", id);
		AuditLogResponse response = auditLogService.getAuditLogById(id);
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Delete Audit Log", description = "Delete audit log record by ID")
	public ResponseEntity<Void> deleteAuditLog(@PathVariable Integer id) {
		logger.info("REST DELETE /audit/{} invoked", id);
		auditLogService.deleteAuditLog(id);
		return ResponseEntity.noContent().build();
	}
}
