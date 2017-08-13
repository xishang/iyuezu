package com.iyuezu.common.exceptions;

public class PasswordIncorrectException extends Exception {

	private static final long serialVersionUID = 1188245948092605897L;

	public PasswordIncorrectException() {
		super();
	}

	public PasswordIncorrectException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public PasswordIncorrectException(String message, Throwable cause) {
		super(message, cause);
	}

	public PasswordIncorrectException(String message) {
		super(message);
	}

	public PasswordIncorrectException(Throwable cause) {
		super(cause);
	}

}
