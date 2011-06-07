/**
 * This file contains the ECompileFailed exception.
 */
package org.datasift;

/**
 * Thrown when a compile request fails due to errors in the definition.
 * 
 * @author MediaSift
 * @version 0.1
 */
public class ECompileFailed extends Exception {
	/**
	 * Required by the superclass.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public ECompileFailed() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public ECompileFailed(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public ECompileFailed(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ECompileFailed(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}
}
