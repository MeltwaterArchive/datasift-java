/**
 * This file contains the Destination class.
 */
package org.datasift;

import java.io.Serializable;
import java.util.HashMap;

/**
 * The PushDefinition represents the configuration of a push endpoint.
 * 
 * @author MediaSift
 * @version 0.1
 */
public class PushDefinition implements Serializable {
	/**
	 * Auto-generated serialization version UID.
	 */
	private static final long serialVersionUID = -4980171840666548806L;

	public static String OUTPUT_PARAMS_PREFIX = "output_params.";
	
	protected User _user = null;
	protected String _name = "";
	protected String _initial_status = "";
	protected String _output_type = "";
	protected PushOutputParams _output_params = new PushOutputParams();
	
	protected PushDefinition(User user) {
		_user = user;
	}
	
	public String getName() {
		return _name;
	}
	
	public void setName(String name) throws EInvalidData {
		_name = name;
	}
	
	public String getInitialStatus() {
		return _initial_status;
	}
	
	public void setInitialStatus(String status) {
		_initial_status = status;
	}
	
	public String getOutputType() {
		return _output_type;
	}
	
	public void setOutputType(String type) {
		_output_type = type;
	}
	
	public void setOutputParam(String key, String val) throws EInvalidData {
		_output_params.put(key, val);
	}

	public String getOutputParam(String key) {
		return _output_params.get(key);
	}
	
	public PushOutputParams getOutputParams() {
		return _output_params;
	}
	
	public void validate() throws EAccessDenied, EInvalidData {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("output_type", _output_type);
		for (String key : _output_params.keySet()) {
			params.put(OUTPUT_PARAMS_PREFIX + key, _output_params.get(key));
		}
		
		try {
			_user.callAPI("push/validate", params);
		} catch (EAPIError e) {
			throw new EInvalidData(e.getMessage());
		}
	}
	
	public PushSubscription subscribe(Definition definition) throws EInvalidData, EAccessDenied, EAPIError {
		return subscribeStreamHash(definition.getHash());
	}
	
	public PushSubscription subscribeStreamHash(String hash) throws EInvalidData, EAPIError, EAccessDenied {
		return subscribe("hash", hash);
	}
	
	public PushSubscription subscribe(Historic historic) throws EInvalidData, EAccessDenied, EAPIError {
		return subscribeHistoricPlaybackId(historic.getHash());
	}
	
	public PushSubscription subscribeHistoricPlaybackId(String hash) throws EInvalidData, EAPIError, EAccessDenied {
		return subscribe("playback_id", hash);
	}
	
	protected PushSubscription subscribe(String hash_type, String hash) throws EInvalidData, EAPIError, EAccessDenied {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("name", getName());
		params.put(hash_type, hash);
		params.put("output_type", getOutputType());
		for (String key : _output_params.keySet()) {
			params.put("output_params." + key, _output_params.get(key));
		}

		// Add the initial status if it's not empty
		if (getInitialStatus().length() > 0) {
			params.put("initial_status", getInitialStatus());
		}

		// Call the API and create a new PushSubscription from the returned
		// object
		return new PushSubscription(_user, _user.callAPI("push/create", params));
	}
}
