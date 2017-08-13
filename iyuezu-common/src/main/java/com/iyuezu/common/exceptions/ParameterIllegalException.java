package com.iyuezu.common.exceptions;

public class ParameterIllegalException extends Exception {

	private static final long serialVersionUID = 3696934735212624966L;

	public ParameterIllegalException() {
		super();
	}

	public ParameterIllegalException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ParameterIllegalException(String message, Throwable cause) {
		super(message, cause);
	}

	public ParameterIllegalException(String message) {
		super(message);
	}

	public ParameterIllegalException(Throwable cause) {
		super(cause);
	}

}
