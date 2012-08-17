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
public class ECompileFailed extends DataSiftException {
	/**
	 * Required by the superclass.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public ECompileFailed() {
	}

	/**
	 * @param message
	 */
	public ECompileFailed(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public ECompileFailed(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ECompileFailed(String message, Throwable cause) {
		super(message, cause);
	}
}
