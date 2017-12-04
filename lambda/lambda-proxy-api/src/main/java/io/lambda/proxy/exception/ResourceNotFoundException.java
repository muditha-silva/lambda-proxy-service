package io.lambda.proxy.exception;

/**
 * 
 * @author muditha
 *
 */
public class ResourceNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -205581906087753749L;

	public ResourceNotFoundException() {
		super();		
	}

	public ResourceNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);		
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
