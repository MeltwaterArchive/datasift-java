/**
 * This file contains the EInvalidData exception.
 */
package org.datasift;

/**
 * @author MediaSift
 * @version 0.1
 */
public class EInvalidData extends DataSiftException {
	/**
	 * Required by the superclass.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public EInvalidData() {
	}

	/**
	 * @param arg0
	 */
	public EInvalidData(String arg0) {
		super(arg0);
	}
}
