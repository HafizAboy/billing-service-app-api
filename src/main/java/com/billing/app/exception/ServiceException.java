package com.billing.app.exception;

/**
 * @author Hafiz
 */
public class ServiceException extends Exception {
	private static final long serialVersionUID = 1L;

	private final String errorCode;
	
	public ServiceException(String errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}
	
	public ServiceException(String errorCode, String message, Throwable cause) {
		super(message, cause);
		this.errorCode = errorCode;
	}

	public String getErrorCode() {
		return errorCode;
	}

}