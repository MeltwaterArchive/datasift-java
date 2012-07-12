package org.datasift.pushsubscription;

import org.datasift.EInvalidData;
import org.datasift.PushSubscription;
import org.datasift.User;
import org.json.JSONException;
import org.json.JSONObject;

public class Http extends PushSubscription {
	public static final String AUTH_TYPE_NONE = "none";
	public static final String AUTH_TYPE_BASIC = "basic";

	public Http(User user) {
		super(user);
	}

	public Http(User user, JSONObject json) throws EInvalidData {
		super(user, json);
	}
	
	public void setUrl(String url) throws EInvalidData {
		if (isDeleted()) {
			throw new EInvalidData("Cannot modify a deleted subscription");
		}

		try {
			_output_params.put("url", url);
		} catch (JSONException e) {
			throw new EInvalidData("Failed to update the URL");
		}
	}
	
	public String getUrl() {
		try {
			return _output_params.getString("url");
		} catch (JSONException e) {
			return "";
		}
	}
	
	public void setMaxSize(int bytes) throws EInvalidData {
		if (isDeleted()) {
			throw new EInvalidData("Cannot modify a deleted subscription");
		}

		try {
			_output_params.put("max_size", bytes);
		} catch (JSONException e) {
			throw new EInvalidData("Failed to update the max_size");
		}
	}
	
	public void removeMaxSize() {
		_output_params.remove("max_size");
	}
	
	public int getMaxSize() {
		try {
			return _output_params.getInt("max_size");
		} catch (JSONException e) {
			return 0;
		}
	}

	public void setAuthType(String auth_type) throws EInvalidData {
		if (isDeleted()) {
			throw new EInvalidData("Cannot modify a deleted subscription");
		}

		JSONObject auth = getAuthObject();
		try {
			auth.put("type", auth_type);
		} catch (JSONException e) {
			throw new EInvalidData("Failed to update the auth type");
		}
		setAuthObject(auth);
	}
	
	public String getAuthType() {
		try {
			return getAuthObject().getString("type");
		} catch (JSONException e) {
			return "";
		}
	}

	public void setAuthUsername(String username) throws EInvalidData {
		if (isDeleted()) {
			throw new EInvalidData("Cannot modify a deleted subscription");
		}

		JSONObject auth = getAuthObject();
		try {
			auth.put("username", username);
		} catch (JSONException e) {
			throw new EInvalidData("Failed to update the auth username");
		}
		setAuthObject(auth);
	}
	
	public String getAuthUsername() {
		try {
			return getAuthObject().getString("username");
		} catch (JSONException e) {
			return "";
		}
	}

	public void setAuthPassword(String password) throws EInvalidData {
		if (isDeleted()) {
			throw new EInvalidData("Cannot modify a deleted subscription");
		}

		JSONObject auth = getAuthObject();
		try {
			auth.put("password", password);
		} catch (JSONException e) {
			throw new EInvalidData("Failed to update the auth password");
		}
		setAuthObject(auth);
	}
	
	public String getAuthPassword() {
		try {
			return getAuthObject().getString("password");
		} catch (JSONException e) {
			return "";
		}
	}
	
	private JSONObject getAuthObject() {
		try {
			return _output_params.getJSONObject("auth");
		} catch (JSONException e) {
			return new JSONObject();
		}
	}
	
	private void setAuthObject(JSONObject auth) throws EInvalidData {
		if (isDeleted()) {
			throw new EInvalidData("Cannot modify a deleted subscription");
		}

		try {
			_output_params.put("auth", auth);
		} catch (JSONException e) {
			throw new EInvalidData("Failed to update auth settings");
		}
	}
}
