/**
 * This file contains the User class.
 */
package org.datasift;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * The User class represents a user of the API. Applications should start their
 * API interactions by creating an instance of this class. Once initialised it
 * provides factory methods for all of the functionality in the API.
 *
 * @author MediaSift
 * @version 0.2
 */
public class User {
	/**
	 * The user agent to be used for all HTTP requests.
	 *
	 * @access public
	 */
	public final static String _user_agent = "DataSiftJava/1.0.0";

	/**
	 * The base URL for API calls. No http://, and with the trailing slash.
	 *
	 * @access public
	 */
	public static String _api_base_url = "api.datasift.com/";

	/**
	 * The base URL for HTTP streaming. No http://, and with the trailing slash.
	 *
	 * @access public
	 */
	public static String _stream_base_url = "stream.datasift.com/";

	/**
	 * Usage period constant: hour
	 *
	 * @access public
	 */
	public final static String USAGE_HOUR = "hour";

	/**
	 * Usage period constant: day
	 *
	 * @access public
	 */
	public final static String USAGE_DAY = "day";

	/**
	 * The username of this user.
	 *
	 * @access protected
	 */
	protected String _username = "";

	/**
	 * The API key for this user.
	 *
	 * @access protected
	 */
	protected String _api_key = "";

	/**
	 * The rate limit returned by the last API call.
	 *
	 * @access protected
	 */
	protected int _rate_limit = -1;

	/**
	 * The rate limit remaining returned by the last API call.
	 *
	 * @access protected
	 */
	protected int _rate_limit_remaining = -1;

	/**
	 * The object to use for making API calls.
	 *
	 * @access protected
	 */
	protected ApiClient _api_client = null;

	/**
	 * Constructor. A username and API key are required when constructing an
	 * instance of this class.
	 *
	 * @access public
	 * @param String
	 *            username The user's username.
	 * @param String
	 *            api_key The user's API key.
	 * @throws EInvalidData
	 */
	public User(String username, String api_key) throws EInvalidData {
		if (username.length() == 0) {
			throw new EInvalidData(
				"Please supply valid credentials when creating a DataSift_User object."
			);
		}

		if (api_key.length() == 0) {
			throw new EInvalidData(
				"Please supply valid credentials when creating a DataSift_User object."
			);
		}

		_username = username;
		_api_key = api_key;
	}

	/**
	 * Returns the username.
	 *
	 * @access public
	 * @return String The username.
	 */
	public String getUsername() {
		return _username;
	}

	/**
	 * Returns the API key.
	 *
	 * @access public
	 * @return String The API key.
	 */
	public String getAPIKey() {
		return _api_key;
	}

	/**
	 * Returns the rate limit returned by the last API call.
	 *
	 * @access public
	 * @return int The rate limit.
	 */
	public int getRateLimit() {
		return _rate_limit;
	}

	/**
	 * Returns the rate limit remaining returned by the last API call.
	 *
	 * @access public
	 * @return int The rate limit remaining.
	 */
	public int getRateLimitRemaining() {
		return _rate_limit_remaining;
	}

	/**
	 * Returns the base URL for HTTP stream request.
	 *
	 * @access public
	 * @return String The stream base URL.
	 */
	public String getStreamBaseURL() {
		return _stream_base_url;
	}

	/**
	 * Set the ApiClient object to use when accessing the API.
	 *
	 * @param api_client
	 */
	public void setApiClient(ApiClient api_client) {
		_api_client = api_client;
	}

	/**
	 * Creates and returns an empty Definition object.
	 *
	 * @access public
	 * @return Definition A definition object tied to this user.
	 */
	public Definition createDefinition() {
		return createDefinition("");
	}

	/**
	 * Creates and returns a Definition object based on the given CSDL.
	 *
	 * @access public
	 * @param String
	 *            csdl CSDL with which to prime the object.
	 * @return Definition A definition object tied to this user.
	 */
	public Definition createDefinition(String csdl) {
		return new Definition(this, csdl);
	}

	/**
	 * Get usage data for this user.
	 *
	 * @access public
	 * @param String
	 *            hash Specifies the stream hash in which we're interested.
	 * @return Usage
	 * @throws EAccessDenied
	 * @throws EAPIError
	 * @throws EInvalidData
	 */
	public Usage getUsage() throws EAPIError, EAccessDenied,
			EInvalidData {
		return getUsage(User.USAGE_HOUR);
	}

	/**
	 * Get usage data for this user.
	 *
	 * @access public
	 * @param String period Use the final static vars in this class to specify
	 *            either "day" or "hour".
	 * @return Usage
	 * @throws EAccessDenied
	 * @throws EAPIError
	 * @throws EInvalidData
	 */
	public Usage getUsage(String period) throws EAPIError,
			EAccessDenied, EInvalidData {
		HashMap<String, String> params = new HashMap<String, String>();

		if (period != User.USAGE_HOUR && period != User.USAGE_DAY) {
			throw new EInvalidData("The specified period is not supported");
		}

		params.put("period", period);

		JSONObject res = callAPI("usage", params);
		try {
			return new Usage(res.toString());
		} catch (JSONException e) {
			throw new EAPIError(
					"There was an issue parsing the response from DataSift: "
							+ e.toString());
		} catch (EInvalidData e) {
			throw new EAPIError(
					"There was an issue parsing the response from DataSift: "
							+ e.toString());
		}
	}

	/**
	 * Returns the user agent this library should use for all API calls.
	 *
	 * @access public
	 * @returns String The user agent.
	 */
	public String getUserAgent() {
		return _user_agent;
	}

	/**
	 * Make a call to a DataSift API endpoint.
	 *
	 * @access public
	 * @param String
	 *            endpoint The endpoint of the API call.
	 * @param HashMap
	 *            params The parameters to be passed along with the request.
	 * @throws EAPIError
	 * @return HashMap The response from the server.
	 * @throws EInvalidData
	 * @throws EAccessDenied
	 */
	public JSONObject callAPI(String endpoint, HashMap<String, String> params)
		throws EAPIError, EAccessDenied
	{
		JSONObject retval = null;

		// Create the default ApiClient object if we don't already have one
		if (_api_client == null) {
			_api_client = new ApiClient(this);
		}

		// Make the API call
		ApiResponse res = _api_client.call(endpoint, params);

		if (res != null) {
			_rate_limit = res.getRateLimit();
			_rate_limit_remaining = res.getRateLimitRemaining();

			String str = res.getBody();
			if (str.length() > 0) {
				try {
					retval = new JSONObject(str);
				} catch (JSONException e) {
					throw new EAPIError(
						"There was an issue parsing the response from DataSift. Status code: "
						+ res.getStatusCode() + " "
						+ res.getReasonPhrase()
					);
				}
			}

			try {
				switch (res.getStatusCode()) {
				case 200:
					break;

				case 400:
					throw new EAPIError(
						(retval != null && retval.has("error")) ? retval.getString("error")
							: "No error message present!",
						res.getStatusCode()
					);

				case 401:
					throw new EAccessDenied(
						(String) ((retval != null && retval.has("error")) ? retval
							.get("error") : "Authentication failed!")
					);

				case 403:
					throw new EAPIError(
						(retval != null && retval.has("error")) ? retval
							.getString("error")
							: "The supplied credentials didn't exist or were incorrect.",
						res.getStatusCode()
					);

				case 404:
					throw new EAPIError(
						(retval != null && retval.has("error")) ? retval.getString("error")
							: "Method or stream doesn't exist!",
						res.getStatusCode()
					);

				case 500:
					throw new EAPIError(
						(retval != null && retval.has("error")) ? retval
							.getString("error")
							: "There was an internal server problem with DataSift - it may be down for maintenance!",
						res.getStatusCode()
					);

				default:
					throw new EAPIError(
						(retval != null && retval.has("error")) ? retval.getString("error")
							: "An unrecognised error has occured. Status code: "
							+ res.getStatusCode()
							+ " "
							+ res.getReasonPhrase(),
						res.getStatusCode()
					);
				}
			} catch (JSONException e) {
				throw new EAPIError("Failed to parse JSON response: " + e.getMessage());
			}
		}

		return retval;
	}
}
