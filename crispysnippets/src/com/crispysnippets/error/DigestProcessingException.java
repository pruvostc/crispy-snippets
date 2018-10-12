package com.crispysnippets.error;

public class DigestProcessingException extends Exception {

	public DigestProcessingException() {
		super();
	}
	
	public DigestProcessingException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public DigestProcessingException(String message) {
		super(message);
	}

	public DigestProcessingException(Throwable throwable) {
		super(throwable);
	}
}