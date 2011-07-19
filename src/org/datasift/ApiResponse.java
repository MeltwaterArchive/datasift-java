/**
 * This file contains the ApiResponse class.
 */
package org.datasift;

/**
 * The ApiResponse class wraps the data returned by calls to the API.
 * 
 * @author MediaSift
 * @version 0.3
 */
public class ApiResponse {

	private int _status_code;
	private String _reason_phrase;
	private String _body;
	private int _rate_limit;
	private int _rate_limit_remaining;

	/**
	 * Constructor. ApiResponse objects are read-only, so all data is passed
	 * into the constructor.
	 * 
	 * @param status_code
	 * @param reason_phrase
	 * @param body
	 * @param rate_limit
	 * @param rate_limit_remaining
	 */
	public ApiResponse(int status_code, String reason_phrase, String body,
			int rate_limit, int rate_limit_remaining) {
		_status_code = status_code;
		_reason_phrase = reason_phrase;
		_body = body;
		_rate_limit = rate_limit;
		_rate_limit_remaining = rate_limit_remaining;
	}

	/**
	 * @return the status code
	 */
	public int getStatusCode() {
		return _status_code;
	}

	/**
	 * @return the status code
	 */
	public String getReasonPhrase() {
		return _reason_phrase;
	}

	/**
	 * @return the body
	 */
	public String getBody() {
		return _body;
	}

	/**
	 * @return the rate limit
	 */
	public int getRateLimit() {
		return _rate_limit;
	}

	/**
	 * @return the _statusCode
	 */
	public int getRateLimitRemaining() {
		return _rate_limit_remaining;
	}
}
