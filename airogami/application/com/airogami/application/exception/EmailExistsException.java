package com.airogami.application.exception;

public class EmailExistsException extends ApplicationException {

	public EmailExistsException(){
		super("Duplicate email");
	}
}
