package com.iyuezu.common.exceptions;

public class AccountAlreadyExistException extends Exception {

	private static final long serialVersionUID = 4777370280374724447L;

	public AccountAlreadyExistException() {
		super();
	}

	public AccountAlreadyExistException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public AccountAlreadyExistException(String message, Throwable cause) {
		super(message, cause);
	}

	public AccountAlreadyExistException(String message) {
		super(message);
	}

	public AccountAlreadyExistException(Throwable cause) {
		super(cause);
	}

}
