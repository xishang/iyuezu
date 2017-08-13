package com.iyuezu.common.exceptions;

public class PhoneAlreadyExistException extends Exception {

	private static final long serialVersionUID = 5645120600177531814L;

	public PhoneAlreadyExistException() {
		super();
	}

	public PhoneAlreadyExistException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public PhoneAlreadyExistException(String message, Throwable cause) {
		super(message, cause);
	}

	public PhoneAlreadyExistException(String message) {
		super(message);
	}

	public PhoneAlreadyExistException(Throwable cause) {
		super(cause);
	}

}
