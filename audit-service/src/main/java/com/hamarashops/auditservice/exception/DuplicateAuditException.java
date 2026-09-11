package com.hamarashops.auditservice.exception;

public class DuplicateAuditException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DuplicateAuditException(String message) {
		super(message);
	}
}
