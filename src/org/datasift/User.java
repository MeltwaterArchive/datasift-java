/**
 * This file contains the User class.
 */
package org.datasift;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

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
	 * @access private
	 */
	private String _user_agent = "DataSoftJava/0.2";

	/**
	 * The base URL for API calls. No http://, and with the trailing slash.
	 * 
	 * @access private
	 */
	private String _api_base_url = "api.datasift.net/";

	/**
	 * The base URL for HTTP streaming. No http://, and with the trailing slash.
	 * 
	 * @access private
	 */
	private String _stream_base_url = "stream.datasift.net/";

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
	 * @param Hashtable
	 *            params The parameters to be passed along with the request.
	 * @throws EAPIError
	 * @return Hashtable The response from the server.
	 * @throws EInvalidData
	 * @throws EAccessDenied
	 */
	public JSONObject callAPI(String endpoint, Hashtable<String, String> params)
			throws EAPIError, EInvalidData, EAccessDenied {
		JSONObject retval = null;

		HttpClient httpClient = new DefaultHttpClient();

		try {
			List<NameValuePair> qparams = new ArrayList<NameValuePair>();
			Enumeration<String> paramsEnum = params.keys();
			while (paramsEnum.hasMoreElements()) {
				String key = paramsEnum.nextElement();
				qparams.add(new BasicNameValuePair(key, params.get(key)));
			}

			HttpGet get = new HttpGet("http://" + this._api_base_url + endpoint
					+ ".json?" + URLEncodedUtils.format(qparams, "UTF-8"));
			get.addHeader("Auth", getUsername() + ":" + getAPIKey());

			HttpResponse response = httpClient.execute(get);

			HttpEntity entity = response.getEntity();

			// Update the rate limits from the headers
			_rate_limit = -1;
			_rate_limit_remaining = -1;
			Header[] headers = response.getAllHeaders();
			for (int i = 0; i < headers.length; i++) {
				String name = headers[i].getName();
				if (name.equalsIgnoreCase("x-ratelimit-limit")) {
					_rate_limit = Integer.parseInt(headers[i].getValue());
				} else if (name.equalsIgnoreCase("x-ratelimit-remaining")) {
					_rate_limit_remaining = Integer.parseInt(headers[i]
							.getValue());
				}
			}

			String res = "";
			if (entity != null) {
				res = EntityUtils.toString(entity);
			}

			if (res.length() > 0) {
				try {
					retval = new JSONObject(res);
				} catch (JSONException e) {
					throw new EAPIError(
							"There was an issue parsing the response from DataSift. Status code: "
									+ response.getStatusLine().getStatusCode()
									+ " "
									+ response.getStatusLine()
											.getReasonPhrase());
				}
			}

			switch (response.getStatusLine().getStatusCode()) {
			case 200:
				break;

			case 401:
				throw new EAccessDenied(
						(String) ((retval != null && retval.has("error")) ? retval
								.get("error") : "Authentication failed"));

			case 403:
				throw new EAPIError(
						(retval != null && retval.has("error")) ? retval
								.getString("error")
								: "The supplied credentials didn't exist or were incorrect.");

			case 404:
				throw new EAPIError(
						(retval != null && retval.has("error")) ? retval
								.getString("error")
								: "Method or stream doesn't exist");

			case 500:
				throw new EAPIError(
						(retval != null && retval.has("error")) ? retval
								.getString("error")
								: "There was an internal server problem with DataSift - it may be down for maintenance");

			default:
				throw new EAPIError(
						(retval != null && retval.has("error")) ? retval
								.getString("error")
								: "An unrecognised error has occured. Status code: "
										+ response.getStatusLine()
												.getStatusCode()
										+ " "
										+ response.getStatusLine()
												.getReasonPhrase());
			}
		} catch (ParseException e) {
			throw new EAPIError("Failed to parse response: " + e.getMessage());
		} catch (IOException e) {
			throw new EAPIError("Network error: " + e.getMessage());
		} catch (JSONException e) {
			throw new EAPIError("Failed to parse JSON response: "
					+ e.getMessage());
		} finally {
			httpClient.getConnectionManager().shutdown();
		}

		return retval;
	}
}
