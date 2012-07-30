/**
 * This file contains the LogEntry class.
 */
package org.datasift.pushsubscription;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A LogEntry object represents a single entry in a push subscription log.
 * 
 * @author MediaSift
 * @version 0.1
 */
public class LogEntry {
	/**
	 * @var String The subscription ID.
	 */
	private String _subscription_id = "";
	
	/**
	 * @var Date The date/time of the log entry.
	 */
	private Date _request_time = null;
	
	/**
	 * @var boolean True if this entry is reporting a successful action.
	 */
	private boolean _success = false;
	
	/**
	 * @var String The log message.
	 */
	private String _message = "";
	
	/**
	 * Construct an instance from the data in a JSONObject.
	 * 
	 * @param JSONObject json The data.
	 * @throws JSONException
	 */
	public LogEntry(JSONObject json) throws JSONException {
		this(json.getString("subscription_id"), json.getLong("request_time"), json.getBoolean("success"), (json.has("message") ? json.getString("message") : ""));
	}
	
	/**
	 * Construct an instance from individual variables.
	 * 
	 * @param String  subscription_id The subscription ID.
	 * @param long    request_time    The unix timestamp of the entry.
	 * @param boolean success         True if this entry is reporting a successful operation.
	 */
	public LogEntry(String subscription_id, long request_time, boolean success) {
		this(subscription_id, request_time, success, "");
	}
	
	/**
	 * Construct an instance from individual variables.
	 * 
	 * @param String  subscription_id The subscription ID.
	 * @param long    request_time    The unix timestamp of the entry.
	 * @param boolean success         True if this entry is reporting a successful operation.
	 * @param String  message         The log message.
	 */
	public LogEntry(String subscription_id, long request_time, boolean success, String message) {
		_subscription_id = subscription_id;
		_request_time = new Date(request_time * 1000);
		_success = success;
		_message = message;
	}
	
	/**
	 * Get the subscription ID.
	 * 
	 * @return String
	 */
	public String getSubscriptionId() {
		return _subscription_id;
	}
	
	/**
	 * Get the request time.
	 * 
	 * @return Date
	 */
	public Date getRequestTime() {
		return _request_time;
	}
	
	/**
	 * Get whether this entry is reporting a successful action.
	 * 
	 * @return boolean
	 */
	public boolean getSuccess() {
		return _success;
	}
	
	/**
	 * Get the log message.
	 * 
	 * @return String
	 */
	public String getMessage() {
		return _message;
	}
}
