package com.hamarashops.documentservice.exception;

public class DuplicateDocumentException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DuplicateDocumentException(String message) {
		super(message);
	}
}
