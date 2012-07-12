/**
 * This file contains the EAPIError exception.
 */
package org.datasift;

/**
 * @author MediaSift
 * @version 0.1
 */
public class EAPIError extends DataSiftException {
	/**
	 * Required by the superclass.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The code number of this exception.
	 */
	private int _code = -1;

	/**
	 * @param arg0
	 */
	public EAPIError(String arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 */
	public EAPIError(String arg0, int code) {
		super(arg0);
		this._code = code;
	}

	/**
	 * Get the code for this exception.
	 * @return
	 */
	public int getCode() {
		return _code;
	}
}
