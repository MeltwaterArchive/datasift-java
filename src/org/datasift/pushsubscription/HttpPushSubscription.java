package org.datasift.pushsubscription;

import org.datasift.EInvalidData;
import org.datasift.PushSubscription;
import org.datasift.User;
import org.json.JSONObject;

public class HttpOutputType extends PushSubscription {
	public HttpOutputType(User user) {
		super(user);
		initHttp();
	}

	public HttpOutputType(User user, JSONObject json) throws EInvalidData {
		super(user, json);
		initHttp();
	}
	
	public void initHttp() {
		if (!_output_params.containsKey("delivery_frequency")) {
			_output_params.put("delivery_frequency", "10");
		}
		if (!_output_params.containsKey("url")) {
			_output_params.put("url", "");
		}
		if (!_output_params.containsKey("auth.type")) {
			_output_params.put("auth.type", "none");
		}
		if (!_output_params.containsKey("auth.username")) {
			_output_params.put("auth.username", "");
		}
		if (!_output_params.containsKey("auth.password")) {
			_output_params.put("auth.password", "");
		}
	}
	
	public void setDeliveryFrequency(int freq) throws EInvalidData {
		if (isDeleted()) {
			throw new EInvalidData("Cannot modify a deleted subscription");
		}
		
		_output_params.put("delivery_frequency", String.valueOf(freq));
	}
	
	public int getDeliveryFrequency() {
		if (_output_params.containsKey("delivery_frequency")) {
			return Integer.parseInt(_output_params.get("delivery_frequency"));
		}
		return 10;
	}
	
	public void setUrl(String url) throws EInvalidData {
		if (isDeleted()) {
			throw new EInvalidData("Cannot modify a deleted subscription");
		}

		_output_params.put("url", url);
	}
	
	public String getUrl() {
		if (_output_params.containsKey("url")) {
			return _output_params.get("url");
		}
		return "";
	}
	
	public void setMaxSize(int bytes) throws EInvalidData {
		if (isDeleted()) {
			throw new EInvalidData("Cannot modify a deleted subscription");
		}

		_output_params.put("max_size", String.valueOf(bytes));
	}
	
	public void removeMaxSize() {
		_output_params.remove("max_size");
	}
	
	public int getMaxSize() {
		if (_output_params.containsKey("max_size")) {
			return Integer.parseInt(_output_params.get("max_size"));
		}
		return 0;
	}

	public void setAuthType(String auth_type) throws EInvalidData {
		if (isDeleted()) {
			throw new EInvalidData("Cannot modify a deleted subscription");
		}

		_output_params.put("auth.type", auth_type);
	}
	
	public String getAuthType() {
		if (_output_params.containsKey("auth.type")) {
			return _output_params.get("auth.type");
		}
		return "";
	}

	public void setAuthUsername(String username) throws EInvalidData {
		if (isDeleted()) {
			throw new EInvalidData("Cannot modify a deleted subscription");
		}

		_output_params.put("auth.username", username);
	}
	
	public String getAuthUsername() {
		return _output_params.get("auth.username");
	}

	public void setAuthPassword(String password) throws EInvalidData {
		if (isDeleted()) {
			throw new EInvalidData("Cannot modify a deleted subscription");
		}

		_output_params.put("auth.password", password);
	}
	
	public String getAuthPassword() {
		if (_output_params.containsKey("auth.password")) {
			return _output_params.get("auth.password");
		}
		return "";
	}
}
