package com.org.Transaction.exception;

public class AccountNotExistsException extends Exception {
	
	private String message;
	
	public AccountNotExistsException(String message) {
		this.message = message;
	}
	
}
