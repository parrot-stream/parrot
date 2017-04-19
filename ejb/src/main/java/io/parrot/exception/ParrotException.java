package io.parrot.exception;

public class ParrotException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private String errorCode;

	public ParrotException(String message, Throwable cause) {
		super(message, cause);
	}

	public ParrotException(String message) {
		super(message);
	}

	public ParrotException(String errorCode, String message) {
		super(message);
		setErrorCode(errorCode);
	}

	public ParrotException(String errorCode, String message, Throwable cause) {
		super(message, cause);
		setErrorCode(errorCode);
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
}
