package com.healthcare.exception;

/**
 * 
 * @author orange
 *
 */
public class ResourceNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 6398831447858389090L;

	public ResourceNotFoundException() {
		super();
	}

	public ResourceNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public ResourceNotFoundException(String message) {
		super(message);
	}

	public ResourceNotFoundException(Throwable cause) {
		super(cause);
	}

}
