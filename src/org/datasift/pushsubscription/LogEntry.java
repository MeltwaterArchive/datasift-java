package org.datasift.pushsubscription;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

public class LogEntry {
	private String _subscription_id = "";
	private Date _request_time = null;
	private boolean _success = false;
	private String _message = "";
	
	public LogEntry(JSONObject json) throws JSONException {
		this(json.getString("subscription_id"), json.getLong("request_time"), json.getBoolean("success"), (json.has("message") ? json.getString("message") : ""));
	}
	
	public LogEntry(String subscription_id, long request_time, boolean success) {
		this(subscription_id, request_time, success, "");
	}
	
	public LogEntry(String subscription_id, long request_time, boolean success, String message) {
		_subscription_id = subscription_id;
		_request_time = new Date(request_time * 1000);
		_success = success;
		_message = message;
	}
	
	public String getSubscriptionId() {
		return _subscription_id;
	}
	
	public Date getRequestTime() {
		return _request_time;
	}
	
	public boolean getSuccess() {
		return _success;
	}
	
	public String getMessage() {
		return _message;
	}
}
