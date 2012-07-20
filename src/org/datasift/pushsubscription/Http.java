package org.datasift.pushsubscription;

import java.util.HashMap;

import org.datasift.EInvalidData;
import org.datasift.PushSubscription;
import org.datasift.User;
import org.json.JSONException;
import org.json.JSONObject;

public class Http extends PushSubscription {
	protected int _delivery_frequency = 10;
	protected int _max_size = 0;
	protected String _url = "";
	protected String _auth_type = "none";
	protected String _auth_username = "";
	protected String _auth_password = "";
	
	@Override
	protected void setOutputParams(JSONObject output_params) throws JSONException {
		if (output_params.has("delivery_frequency")) {
			_delivery_frequency = output_params.getInt("delivery_frequency");
		}

		if (output_params.has("max_size")) {
			_max_size = output_params.getInt("max_size");
		}

		if (output_params.has("auth")) {
			JSONObject auth = output_params.getJSONObject("auth");
			if (auth.has("type")) {
				_auth_type = auth.getString("type");
			}
			
			if (auth.has("username")) {
				_auth_username = auth.getString("username");
			}

			if (auth.has("password")) {
				_auth_password = auth.getString("password");
			}
		}
	}
	
	@Override
	protected HashMap<String, String> getOutputParams() {
		HashMap<String, String> params = new HashMap<String, String>();
		
		params.put("output_params.delivery_frequency", String.valueOf(_delivery_frequency));
		if (_max_size > 0) {
			params.put("output_params.max_size", String.valueOf(_max_size));
		}
		params.put("output_params.url", _url);
		params.put("output_params.auth.type", _auth_type);
		if (!_auth_type.equals("none")) {
			params.put("output_params.auth.username", _auth_username);
			params.put("output_params.auth.password", _auth_password);
		}
		
		return params;
	}

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

		_url = url;
	}
	
	public String getUrl() {
		return _url;
	}
	
	public void setMaxSize(int bytes) throws EInvalidData {
		if (isDeleted()) {
			throw new EInvalidData("Cannot modify a deleted subscription");
		}

		_max_size = bytes;
	}
	
	public void removeMaxSize() {
		_max_size = 0;
	}
	
	public int getMaxSize() {
		return _max_size;
	}

	public void setAuthType(String auth_type) throws EInvalidData {
		if (isDeleted()) {
			throw new EInvalidData("Cannot modify a deleted subscription");
		}

		_auth_type = auth_type;
	}
	
	public String getAuthType() {
		return _auth_type;
	}

	public void setAuthUsername(String username) throws EInvalidData {
		if (isDeleted()) {
			throw new EInvalidData("Cannot modify a deleted subscription");
		}

		_auth_username = username;
	}
	
	public String getAuthUsername() {
		return _auth_username;
	}

	public void setAuthPassword(String password) throws EInvalidData {
		if (isDeleted()) {
			throw new EInvalidData("Cannot modify a deleted subscription");
		}

		_auth_password = password;
	}
	
	public String getAuthPassword() {
		return _auth_password;
	}
}
