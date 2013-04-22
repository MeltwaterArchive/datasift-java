/**
 * This file contains the User class.
 */
package org.datasift;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.datasift.pushsubscription.Log;
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
	public final static String _user_agent = "DataSiftJava/2.2.0";

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
	 * The base URL for Websocket streaming. No http://, and with the trailing slash.
	 *
	 * @access public
	 */
	public static String _websocket_base_url = "websocket.datasift.com/";

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
	 * Whether we should use SSL where supported.
	 * 
	 * @access protected
	 */
	protected boolean _use_ssl = true;

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
		this(username, api_key, true);
	}
	
	/**
	 * Constructor. A username and API key are required when constructing an
	 * instance of this class.
	 *
	 * @access public
	 * @param String
	 *            username The user's username.
	 * @param String
	 *            api_key The user's API key.
	 * @param boolean
	 *            use_ssl Set to false to disable SSL.
	 * @throws EInvalidData
	 */
	public User(String username, String api_key, boolean use_ssl) throws EInvalidData {
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
		_use_ssl = use_ssl;
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
	 * Returns the base URL for HTTP stream request.
	 *
	 * @access public
	 * @return String The stream base URL.
	 */
	public String getWebsocketBaseURL() {
		return _websocket_base_url;
	}
	
	/**
	 * Enable or disable the use of SSL where supported.
	 * 
	 * @access public
	 * @param use_ssl
	 */
	public void enableSSL(boolean use_ssl) {
		_use_ssl = use_ssl;
	}

	/**
	 * Returns whether SSL should be used where supported.
	 * 
	 * @access public
	 * @return boolean True if SSL should be used.
	 */
	public boolean useSSL() {
		return _use_ssl;
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
	 * @param csdl
	 *            CSDL with which to prime the object.
	 * @return Definition A definition object tied to this user.
	 */
	public Definition createDefinition(String csdl) {
		return new Definition(this, csdl);
	}
	
    /**
     * Create a historic query using the given stream hash.
     * 
     * @param start
     * @param end
     * @param sources
     * @return Historic
     * @throws EInvalidData
     * @throws EAccessDenied
     */
    public Historic createHistoric(String hash, Date start, Date end, String sources, double sample) throws EInvalidData, EAccessDenied
    {
    	return createHistoric(hash, start, end, sources, sample, "");
    }
    
    /**
     * Create a named historic query from this definiton.
     *  
     * @param start
     * @param end
     * @param sources
     * @param name
     * @return Historic
     * @throws EInvalidData
     * @throws EAccessDenied
     */
    public Historic createHistoric(String hash, Date start, Date end, String sources, double sample, String name) throws EInvalidData, EAccessDenied {
    	return new Historic(this, new Definition(this, "", hash), start, end, sources, sample, name);
    }

    /**
     * Get an existing historic.
     * 
     * @param playback_id
     * @return Historic
     * @throws EAPIError 
     * @throws EAccessDenied 
     * @throws EInvalidData 
     */
	public Historic getHistoric(String playback_id) throws EInvalidData, EAccessDenied, EAPIError {
		return new Historic(this, playback_id);
	}
	
	/**
	 * List the first 100 Historics queries.
	 * 
	 * @return ArrayList<Historic>
	 * @throws EInvalidData 
	 * @throws EAccessDenied 
	 * @throws EAPIError 
	 */
	public ArrayList<Historic> listHistorics() throws EAPIError, EAccessDenied, EInvalidData {
		return Historic.list(this);
	}
	
	/**
	 * List Historics queries.
	 * 
	 * @param int page The page to retrieve.
	 * @return ArrayList<Historic>
	 * @throws EInvalidData 
	 * @throws EAccessDenied 
	 * @throws EAPIError 
	 */
	public ArrayList<Historic> listHistorics(int page) throws EAPIError, EAccessDenied, EInvalidData {
		return Historic.list(this, page);
	}
	
	/**
	 * List Historics queries.
	 * 
	 * @param int page     The page to retrieve.
	 * @param int per_page The number of queries per page.
	 * @return ArrayList<Historic>
	 * @throws EInvalidData 
	 * @throws EAccessDenied 
	 * @throws EAPIError 
	 */
	public ArrayList<Historic> listHistorics(int page, int per_page) throws EAPIError, EAccessDenied, EInvalidData {
		return Historic.list(this, page, per_page);
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
     * @return StreamConsumer The consumer object.
     * @throws EInvalidData
     * @throws EAccessDenied
     * @throws ECompileFailed
     */
    public StreamConsumer getConsumer(String type, String hash,
            IStreamConsumerEvents eventHandler) throws EInvalidData,
            ECompileFailed, EAccessDenied {
        return StreamConsumer.factory(this, type, new Definition(this, null, hash), eventHandler);
    }
    
    /**
     * Create a push subscription for this user. Note that you must call the
     * save method to actually create the subscription on the server.
     * 
     * @param String output_type The output_type of the required push subscription.
     * @param String hash_type   The type of hash being supplied (stream or historic).
     * @param String hash        The stream hash or historic playback ID.
     * @param String name        A name for this push subscription.
     * @return PushSubscription
     * @throws EInvalidData
     */
    public PushDefinition createPushDefinition() {
    	return new PushDefinition(this);
    }
    
    /**
     * Get a single push subscription.
     * 
     * @param String id The ID of the subscription to fetch.
     * @return PushSubscription
     * @throws EInvalidData 
     * @throws EAccessDenied 
     * @throws EAPIError 
     */
    public PushSubscription getPushSubscription(String id) throws EAPIError, EAccessDenied, EInvalidData {
    	return PushSubscription.get(this, id);
    }
    
    /**
     * Get a list of push subscriptions in your account.
     * 
     * @return ArrayList<PushSubscription>
     * @throws EInvalidData
     * @throws EAPIError
     * @throws EAccessDenied
     */
    public ArrayList<PushSubscription> listPushSubscriptions() throws EInvalidData, EAPIError, EAccessDenied {
    	return PushSubscription.list(this);
    }
    
    /**
     * Get the most recent push subscription log entries.
     * 
     * @return ArrayList<LogEntry>
     * @throws EAccessDenied 
     * @throws EInvalidData 
     * @throws EAPIError 
     */
    public Log getPushSubscriptionLogs() throws EAPIError, EInvalidData, EAccessDenied {
    	return PushSubscription.getLogs(this);
    }
    
    /**
     * Page through recent push subscription log entries.
     * 
     * @param int page     Which page to fetch.
     * @param int per_page Based on this page size.
     * @return ArrayList<LogEntry>
     * @throws EAccessDenied 
     * @throws EInvalidData 
     * @throws EAPIError 
     */
    public Log getPushSubscriptionLogs(int page, int per_page) throws EAPIError, EInvalidData, EAccessDenied {
    	return PushSubscription.getLogs(this, page, per_page);
    }

    /**
     * Page through recent push subscription log entries, specifying the sort
     * order.
     * 
     * @param int    page      Which page to fetch.
     * @param int    per_page  Based on this page size.
     * @param String order_by  Which field to sort by.
     * @param String order_dir In asc[ending] or desc[ending] order.
     * @return ArrayList<LogEntry>
     * @throws EAccessDenied 
     * @throws EInvalidData 
     * @throws EAPIError 
     */
    public Log getPushSubscriptionLogs(int page, int per_page, String order_by, String order_dir) throws EAPIError, EInvalidData, EAccessDenied {
    	return PushSubscription.getLogs(this, page, per_page, order_by, order_dir);
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
				case 202:
				case 204:
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
