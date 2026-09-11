package com.hamarashops.auditservice.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hamarashops.auditservice.model.AuditLog;

@Repository
public interface AuditLogRepo extends JpaRepository<AuditLog, Integer> {

	List<AuditLog> findByUserId(Integer userId);

	List<AuditLog> findByServiceName(String serviceName);
}
