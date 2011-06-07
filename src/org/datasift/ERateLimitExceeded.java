/**
 * This file contains the ERateLimitExceeded exception.
 */
package org.datasift;

/**
 * This exception gets thrown by the User class when an API call returns an
 * error indicating that the user has exceeded their rate limit.
 * 
 * @author MediaSift
 * @version 0.1
 */
public class ERateLimitExceeded extends Exception {
	/**
	 * Required by the superclass.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public ERateLimitExceeded() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public ERateLimitExceeded(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}
}
