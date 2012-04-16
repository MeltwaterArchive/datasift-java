/**
 * This file contains the User class.
 */
package org.datasift;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
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
	public final static String _user_agent = "DataSiftJava/1.3.2";

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
					"Please supply valid credentials when creating a DataSift_User object.");
		}

		if (api_key.length() == 0) {
			throw new EInvalidData(
					"Please supply valid credentials when creating a DataSift_User object.");
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
     * Returns a DataSift_StreamConsumer-derived object for the given hash,
     * for the given type.
     * 
     * @access public
     * @see StreamConsumer
     * @param String hash The hash of the stream to be consumed.
     * @param String
     *            type The consumer type for which to construct a consumer.
     * @throws EInvalidData
     * @return StreamConsumer The consumer object.
     * @throws EAccessDenied
     * @throws ECompileFailed
     */
    public StreamConsumer getConsumer(String type, String hash,
            IStreamConsumerEvents eventHandler) throws EInvalidData,
            ECompileFailed, EAccessDenied {
        return StreamConsumer.factory(this, type, new Definition(this, null, hash), eventHandler);
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
	 * Get usage data since a given timestamp.
	 * 
	 * @access public
	 * @param int start Specifies the start of the period in which we're
	 *        interested.
	 * @return Usage
	 * @throws EAccessDenied
	 * @throws EAPIError
	 * @throws EInvalidData
	 */
	public Usage getUsageSince(int start) throws EAPIError, EAccessDenied,
			EInvalidData {
		return getUsage(start, 0, "");
	}

	/**
	 * Get usage data for this user.
	 * 
	 * @access public
	 * @param int start Specifies the start of the period in which we're
	 *        interested, or 0 for no start timestamp.
	 * @param int end Specifies the end of the period in which we're interested,
	 *        or 0 for no end timestamp.
	 * @param String
	 *            hash Specifies the stream hash in which we're interested, or
	 *            an empty string for a summary of all streams.
	 * @return Usage
	 * @throws EAccessDenied
	 * @throws EAPIError
	 * @throws EInvalidData
	 */
	public Usage getUsage(int start, int end, String hash) throws EAPIError,
			EAccessDenied, EInvalidData {
		HashMap<String, String> params = new HashMap<String, String>();

		if (start != 0) {
			if (start < 0) {
				throw new EInvalidData(
						"The start parameter must be a positive integer");
			}
			if (end > 0 && start > end) {
				throw new EInvalidData(
						"The start timestamp must not be later than the end timestamp");
			}
			params.put("start", Integer.toString(start));
		}

		if (end != 0) {
			if (end < 0) {
				throw new EInvalidData(
						"The end parameter must be a positive integer");
			}
			params.put("end", Integer.toString(end));
		}

		if (hash.length() > 0) {
			params.put("hash", hash);
		}

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
	 * Returns the recordings stored in this user's DataSift account.
	 * 
	 * @access public
	 * @return ArrayList<Recording>
	 * @throws EInvalidData 
	 * @throws EAccessDenied 
	 * @throws EAPIError 
	 */
	public ArrayList<Recording> getRecordings() throws EInvalidData, EAPIError, EAccessDenied {
		return getRecordings(1, 20);
	}
	
	/**
	 * Returns the given page of recordings from this user's DataSift account.
	 * 
	 * @access public
	 * @param page
	 * @return ArrayList<Recording>
	 * @throws EInvalidData 
	 * @throws EAccessDenied 
	 * @throws EAPIError 
	 */
	public ArrayList<Recording> getRecordings(int page) throws EInvalidData, EAPIError, EAccessDenied {
		return getRecordings(page, 20);
	}
	
	/**
	 * Returns the given page number, for pages of count length, of recordings from this user's DataSift account.
	 * 
	 * @access public
	 * @param page
	 * @param count
	 * @return ArrayList<Recording>
	 * @throws EInvalidData
	 * @throws EAccessDenied 
	 * @throws EAPIError 
	 */
	public ArrayList<Recording> getRecordings(int page, int count) throws EInvalidData, EAPIError, EAccessDenied {
		if (page < 1) {
			throw new EInvalidData("The page parameter must be > 0");
		}
		if (count < 1) {
			throw new EInvalidData("The count parameter must be > 0");
		}
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("page", String.valueOf(page));
		params.put("count", String.valueOf(count));
		
		JSONObject res = callAPI("recording", params);
		
		ArrayList<Recording> retval = new ArrayList<Recording>();
		
		JSONArray recordings;
		try {
			recordings = res.getJSONArray("recordings");
		} catch (JSONException e) {
			throw new EAPIError("Failed to parse the API response: " + e.getMessage());
		}
		
		for (int i = 0; i < recordings.length(); i++) {
			try {
				retval.add(new Recording(this, new JSONdn(recordings.getJSONObject(i).toString())));
			} catch (JSONException e) {
				throw new EAPIError("Failed to parse the API response: " + e.getMessage());
			}
		}
		
		return retval;
	}
	
	/**
	 * Returns a single recording stored in this user's DataSift account.
	 * 
	 * @param id
	 * @return
	 * @throws EAccessDenied 
	 * @throws EAPIError 
	 * @throws EInvalidData 
	 */
	public Recording getRecording(String id) throws EInvalidData, EAPIError, EAccessDenied {
		return new Recording(this, id);
	}
	
	/**
	 * Schedule a new recording.
	 * 
	 * @param hash
	 * @return
	 * @throws EInvalidData 
	 * @throws EAccessDenied 
	 * @throws EAPIError 
	 */
	public Recording scheduleRecording(String hash) throws EInvalidData, EAPIError, EAccessDenied {
		return scheduleRecording(hash, null, 0, 0);
	}

	/**
	 * Schedule a new recording.
	 * 
	 * @param hash
	 * @param name
	 * @return
	 * @throws EInvalidData 
	 * @throws EAccessDenied 
	 * @throws EAPIError 
	 */
	public Recording scheduleRecording(String hash, String name) throws EInvalidData, EAPIError, EAccessDenied {
		return scheduleRecording(hash, name, 0, 0);
	}

	/**
	 * Schedule a new recording.
	 * 
	 * @param hash
	 * @param start
	 * @return
	 * @throws EInvalidData 
	 * @throws EAccessDenied 
	 * @throws EAPIError 
	 */
	public Recording scheduleRecording(String hash, long start) throws EInvalidData, EAPIError, EAccessDenied {
		return scheduleRecording(hash, null, start, 0);
	}

	/**
	 * Schedule a new recording.
	 * 
	 * @param hash
	 * @param start
	 * @param end
	 * @return
	 * @throws EInvalidData 
	 * @throws EAccessDenied 
	 * @throws EAPIError 
	 */
	public Recording scheduleRecording(String hash, long start, int end) throws EInvalidData, EAPIError, EAccessDenied {
		return scheduleRecording(hash, null, start, end);
	}

	/**
	 * Schedule a new recording.
	 * 
	 * @param hash
	 * @param name
	 * @param start
	 * @return
	 * @throws EInvalidData 
	 * @throws EAccessDenied 
	 * @throws EAPIError 
	 */
	public Recording scheduleRecording(String hash, String name, int start) throws EInvalidData, EAPIError, EAccessDenied {
		return scheduleRecording(hash, name, start, 0);
	}

	/**
	 * Schedule a new recording.
	 * 
	 * @param hash
	 * @param name
	 * @param start
	 * @param end
	 * @return
	 * @throws EInvalidData 
	 * @throws EAccessDenied 
	 * @throws EAPIError 
	 */
	public Recording scheduleRecording(String hash, String name, long start, long end) throws EInvalidData, EAPIError, EAccessDenied {
		if (hash == null || hash.length() == 0) {
			throw new EInvalidData("The hash parameter must be supplied to start a recording");
		}
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("hash", hash);
		
		if (name != null && name.length() > 0) {
			params.put("name", name);
		}
		
		if (start != 0) {
			params.put("start", String.valueOf(start));
		}
		
		if (end != 0) {
			if (start != 0 && end < start) {
				throw new EInvalidData("The end timestamp must be later than the start timestamp");
			}
			if (end > 0) {
				params.put("end", String.valueOf(end));
			}
		}
		
		try {
			return new Recording(this, new JSONdn(callAPI("recording/schedule", params).toString()));
		} catch (JSONException e) {
			throw new EAPIError("Failed to parse the API response: " + e.getMessage());
		}
	}
	
	/**
	 * Returns the exports stored in this user's DataSift account.
	 * 
	 * @return
	 * @throws EInvalidData 
	 * @throws EAccessDenied 
	 * @throws EAPIError 
	 */
	public ArrayList<RecordingExport> getExports() throws EInvalidData, EAPIError, EAccessDenied {
		return getExports(1, 20);
	}
	
	/**
	 * Returns the exports stored in this user's DataSift account.
	 * 
	 * @param page
	 * @return
	 * @throws EInvalidData 
	 * @throws EAccessDenied 
	 * @throws EAPIError 
	 */
	public ArrayList<RecordingExport> getExports(int page) throws EInvalidData, EAPIError, EAccessDenied {
		return getExports(page, 20);
	}
	
	/**
	 * Returns the exports stored in this user's DataSift account.
	 * 
	 * @param page
	 * @param count
	 * @return
	 * @throws EInvalidData 
	 * @throws EAccessDenied 
	 * @throws EAPIError 
	 */
	public ArrayList<RecordingExport> getExports(int page, int count) throws EInvalidData, EAPIError, EAccessDenied {
		if (page < 1) {
			throw new EInvalidData("Invalid page number specified");
		}
		if (count < 1) {
			throw new EInvalidData("Invalid page count specified");
		}
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("page", String.valueOf(page));
		params.put("count", String.valueOf(count));
		
		JSONObject res = callAPI("recording/export", params);
		
		ArrayList<RecordingExport> retval = new ArrayList<RecordingExport>();
		
		JSONArray exports;
		try {
			exports = res.getJSONArray("exports");
		} catch (JSONException e) {
			throw new EAPIError("Failed to parse the API response: " + e.getMessage());
		}
		
		for (int i = 0; i < exports.length(); i++) {
			try {
				retval.add(new RecordingExport(this, new JSONdn(exports.getJSONObject(i).toString())));
			} catch (JSONException e) {
				throw new EAPIError("Failed to parse the API response: " + e.getMessage());
			}
		}
		
		return retval;
	}
	
	/**
	 * Returns a single export stored in this user's DataSift account.
	 * 
	 * @param id
	 * @return
	 * @throws EAccessDenied 
	 * @throws EAPIError 
	 * @throws EInvalidData 
	 */
	public RecordingExport getExport(String id) throws EInvalidData, EAPIError, EAccessDenied {
		return new RecordingExport(this, id);
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
									+ res.getReasonPhrase());
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
							res.getStatusCode());

				case 401:
					throw new EAccessDenied(
							(String) ((retval != null && retval.has("error")) ? retval
									.get("error") : "Authentication failed!"));

				case 403:
					throw new EAPIError(
							(retval != null && retval.has("error")) ? retval
									.getString("error")
									: "The supplied credentials didn't exist or were incorrect.",
							res.getStatusCode());

				case 404:
					throw new EAPIError(
							(retval != null && retval.has("error")) ? retval.getString("error")
									: "Method or stream doesn't exist!",
							res.getStatusCode());

				case 500:
					throw new EAPIError(
							(retval != null && retval.has("error")) ? retval
									.getString("error")
									: "There was an internal server problem with DataSift - it may be down for maintenance!",
							res.getStatusCode());

				default:
					throw new EAPIError(
							(retval != null && retval.has("error")) ? retval.getString("error")
									: "An unrecognised error has occured. Status code: "
											+ res.getStatusCode()
											+ " "
											+ res.getReasonPhrase(),
							res.getStatusCode());
				}
			} catch (JSONException e) {
				throw new EAPIError("Failed to parse JSON response: "
						+ e.getMessage());
			}
		}

		return retval;
	}
}
