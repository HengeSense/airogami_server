package com.airogami.application.exception;

public class ApplicationException extends Exception {

	private static final long serialVersionUID = -4163625482976774465L;
	private static final String message = "ApplicationException";
	
	public ApplicationException() {
		super(message);
	}

	public ApplicationException(String message) {
		super(message);
	}

	public ApplicationException(Throwable cause) {
		super(cause);
	}

	public ApplicationException(String message, Throwable cause) {
		super(message, cause);
	}

}
