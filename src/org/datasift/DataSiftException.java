/**
 * This file contains the DataSiftException exception.
 */
package org.datasift;

/**
 * All custom exceptions extend this one to enable a single catch block to
 * catch all exceptions thrown by the library.
 * 
 * @author MediaSift
 * @version 0.1
 */
public class DataSiftException extends Exception {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	public DataSiftException() {
	}

	/**
	 * @param cause
	 */
	public DataSiftException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param arg0
	 */
	public DataSiftException(String arg0) {
		super(arg0);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public DataSiftException(String message, Throwable cause) {
		super(message, cause);
	}
}
