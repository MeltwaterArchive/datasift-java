/**
 * This file contains the MockApiClient class.
 */
package org.datasift;

import java.util.HashMap;

/**
 * The MockApiClient class provides an ApiClient implementation for testing
 * purposes.
 * 
 * @author MediaSift
 * @version 0.3
 */
public class MockApiClient extends ApiClient {

	private ApiResponse _response = null;

	/**
	 * @param user
	 */
	public MockApiClient(User user) {
		super(user);
	}

	/**
	 * Set the response.
	 * 
	 * @param body
	 */
	public void setResponse(String body) {
		setResponse(body, 200, -1, -1, "OK");
	}

	/**
	 * Set the response.
	 * 
	 * @param body
	 * @param status_code
	 */
	public void setResponse(String body, int status_code) {
		setResponse(body, status_code, -1, -1, "OK");
	}

	/**
	 * Set the response.
	 * 
	 * @param body
	 * @param status_code
	 * @param rate_limit
	 * @param rate_limit_remaining
	 */
	public void setResponse(String body, int status_code, int rate_limit,
			int rate_limit_remaining) {
		setResponse(body, status_code, rate_limit, rate_limit_remaining, "OK");
	}

	/**
	 * Set the response.
	 * 
	 * @param body
	 * @param status_code
	 * @param rate_limit
	 * @param rate_limit_remaining
	 * @param reason_phrase
	 */
	public void setResponse(String body, int status_code, int rate_limit,
			int rate_limit_remaining, String reason_phrase) {
		_response = new ApiResponse(status_code, reason_phrase, body,
				rate_limit, rate_limit_remaining);
	}

	/**
	 * Fake a call to the API.
	 * 
	 * @param user
	 * @param endpoint
	 * @param params
	 * @return ApiResponse
	 * @throws EAPIError
	 */
	@Override
	public ApiResponse call(String endpoint, HashMap<String, String> params)
			throws EAPIError {
		
		if (_response == null) {
			throw new EAPIError("Expected response not set in mock object");
		}

		return _response;
	}
}
