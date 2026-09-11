package com.hamarashops.auditservice.service;

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

import com.hamarashops.auditservice.dto.AuditLogRequest;
import com.hamarashops.auditservice.dto.AuditLogResponse;
import com.hamarashops.auditservice.exception.ResourceNotFoundException;
import com.hamarashops.auditservice.model.AuditLog;
import com.hamarashops.auditservice.repo.AuditLogRepo;
import com.hamarashops.auditservice.service.impl.AuditLogServiceImpl;

@ExtendWith(MockitoExtension.class)
class AuditLogServiceTest {

	@Mock
	private AuditLogRepo auditLogRepo;

	@InjectMocks
	private AuditLogServiceImpl auditLogService;

	private AuditLog auditLog;
	private AuditLogRequest request;

	@BeforeEach
	void setUp() {
		auditLog = new AuditLog();
		auditLog.setId(1);
		auditLog.setUserId(101);
		auditLog.setServiceName("USER-SERVICE");
		auditLog.setAction("CREATE_USER");
		auditLog.setEntityName("User");
		auditLog.setEntityId("101");
		auditLog.setDescription("User account created successfully");
		auditLog.setIpAddress("192.168.1.1");
		auditLog.setStatus("SUCCESS");

		request = new AuditLogRequest();
		request.setUserId(101);
		request.setServiceName("USER-SERVICE");
		request.setAction("CREATE_USER");
		request.setEntityName("User");
		request.setEntityId("101");
		request.setDescription("User account created successfully");
		request.setIpAddress("192.168.1.1");
		request.setStatus("SUCCESS");
	}

	@Test
	void testCreateAuditLog_Success() {
		when(auditLogRepo.save(any(AuditLog.class))).thenReturn(auditLog);

		AuditLogResponse response = auditLogService.createAuditLog(request);

		assertNotNull(response);
		assertEquals(1, response.getId());
		assertEquals("USER-SERVICE", response.getServiceName());
		assertEquals("SUCCESS", response.getStatus());
		verify(auditLogRepo).save(any(AuditLog.class));
	}

	@Test
	void testGetAuditLogById_Success() {
		when(auditLogRepo.findById(1)).thenReturn(Optional.of(auditLog));

		AuditLogResponse response = auditLogService.getAuditLogById(1);

		assertNotNull(response);
		assertEquals(1, response.getId());
		assertEquals("CREATE_USER", response.getAction());
	}

	@Test
	void testGetAuditLogById_NotFoundThrowsException() {
		when(auditLogRepo.findById(99)).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> auditLogService.getAuditLogById(99));
	}

	@Test
	void testGetAllAuditLogs_Success() {
		when(auditLogRepo.findAll()).thenReturn(List.of(auditLog));

		List<AuditLogResponse> list = auditLogService.getAllAuditLogs();

		assertEquals(1, list.size());
		assertEquals("USER-SERVICE", list.get(0).getServiceName());
	}
}
