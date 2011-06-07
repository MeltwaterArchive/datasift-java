/**
 * This file contains the EAccessDenied exception.
 */
package org.datasift;

/**
 * Thrown if you try to access DataSift data to which you don't have access.
 * 
 * @author MediaSift
 * @version 0.1
 */
public class EAccessDenied extends Exception {
	/**
	 * Required by the superclass.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public EAccessDenied() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public EAccessDenied(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}
}
