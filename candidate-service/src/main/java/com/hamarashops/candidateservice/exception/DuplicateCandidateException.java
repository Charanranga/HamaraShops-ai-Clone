package com.hamarashops.candidateservice.exception;

public class DuplicateCandidateException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DuplicateCandidateException(String message) {
		super(message);
	}
}
