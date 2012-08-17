/**
 * This file contains the PushDefinition class.
 */
package org.datasift;

import java.io.Serializable;
import java.util.HashMap;

/**
 * A PushDefinition represents the configuration of a push endpoint.
 * 
 * @author MediaSift
 * @version 0.1
 */
public class PushDefinition implements Serializable {
	/**
	 * Auto-generated serialization version UID.
	 */
	private static final long serialVersionUID = -4980171840666548806L;

	/**
	 * The prefix to be used when passing the output_params to API calls.
	 * @var String
	 */
	public static String OUTPUT_PARAMS_PREFIX = "output_params.";
	
	/**
	 * The user that owns this push definition.
	 * @var User
	 */
	protected User _user = null;
	
	/**
	 * An initial status for push subscriptions.
	 * @var String
	 * @see PushSubscription.STATUS_*
	 */
	protected String _initial_status = "";
	
	/**
	 * The output_type of this push definition.
	 * @var String
	 */
	protected String _output_type = "";
	
	/**
	 * The output parameters.
	 * @var PushOutputParams
	 */
	protected PushOutputParams _output_params = new PushOutputParams();
	
	/**
	 * Constructor. Takes the user creating the object.
	 * 
	 * @param User user The user creating this object.
	 */
	public PushDefinition(User user) {
		_user = user;
	}
	
	/**
	 * Get the initial status for subscriptions.
	 * 
	 * @return String
	 * @see PushSubscription.STATUS_*
	 */
	public String getInitialStatus() {
		return _initial_status;
	}
	
	/**
	 * Set the initial status for subscriptions.
	 * 
	 * @param String status The initail status.
	 * @see PushSubscription.STATUS_*
	 */
	public void setInitialStatus(String status) {
		_initial_status = status;
	}
	
	/**
	 * Get the output type.
	 * 
	 * @return String
	 */
	public String getOutputType() {
		return _output_type;
	}
	
	/**
	 * Set the output type.
	 * 
	 * @param String type The output type.
	 */
	public void setOutputType(String type) {
		_output_type = type;
	}
	
	/**
	 * Set an output parameter.
	 * 
	 * @param String key The output parameter to set.
	 * @param String val The value to set it to.
	 * @throws EInvalidData
	 */
	public void setOutputParam(String key, String val) throws EInvalidData {
		_output_params.put(key, val);
	}

	/**
	 * Get an output parameter.
	 * 
	 * @param String key The parameter to get.
	 * @return String
	 */
	public String getOutputParam(String key) {
		return _output_params.get(key);
	}
	
	/**
	 * Get all of the output parameters.
	 * 
	 * @return PushOutputParams
	 */
	public PushOutputParams getOutputParams() {
		return _output_params;
	}
	
	/**
	 * Validate the output type and parameters.
	 * 
	 * @throws EAccessDenied
	 * @throws EInvalidData
	 */
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
	
	/**
	 * Subscribe this endpoint to a Definition.
	 * 
	 * @param Definition definition The definition to which to subscribe.
	 * @param String     name       A name for this subscription.
	 * @return PushSubscription     The new subscription.
	 * @throws EInvalidData
	 * @throws EAccessDenied
	 * @throws EAPIError
	 */
	public PushSubscription subscribe(Definition definition, String name) throws EInvalidData, EAccessDenied, EAPIError {
		return subscribeStreamHash(definition.getHash(), name);
	}
	
	/**
	 * Subscribe this endpoint to a stream hash.
	 * 
	 * @param String hash       The has to which to subscribe.
	 * @param String name       A name for this subscription.
	 * @return PushSubscription The new subscription.
	 * @throws EInvalidData
	 * @throws EAPIError
	 * @throws EAccessDenied
	 */
	public PushSubscription subscribeStreamHash(String hash, String name) throws EInvalidData, EAPIError, EAccessDenied {
		return subscribe("hash", hash, name);
	}
	
	/**
	 * Subscribe this endpoint to a Historic.
	 * 
	 * @param Historic historic The historic object to which to subscribe.
	 * @param String   name     A name for this subscription.
	 * @return PushSubscription The new subscription.
	 * @throws EInvalidData
	 * @throws EAccessDenied
	 * @throws EAPIError
	 */
	public PushSubscription subscribe(Historic historic, String name) throws EInvalidData, EAccessDenied, EAPIError {
		return subscribeHistoricPlaybackId(historic.getHash(), name);
	}
	
	/**
	 * Subscribe this endpoint to a historic playback ID.
	 * 
	 * @param String playback_id The playback ID.
	 * @param String name        A name for this subscription.
	 * @return PushSubscription  The new subscription.
	 * @throws EInvalidData
	 * @throws EAPIError
	 * @throws EAccessDenied
	 */
	public PushSubscription subscribeHistoricPlaybackId(String playback_id, String name) throws EInvalidData, EAPIError, EAccessDenied {
		return subscribe("playback_id", playback_id, name);
	}
	
	/**
	 * Subscribe this endpoint to a stream hash or historic playback ID.
	 * 
	 * @param String hash_type  "hash" or "playback_id"
	 * @param String hash       The hash or playback ID.
	 * @param String name       A name for this subscription.
	 * @return PushSubscription The new subscription.
	 * @throws EInvalidData
	 * @throws EAPIError
	 * @throws EAccessDenied
	 */
	protected PushSubscription subscribe(String hash_type, String hash, String name) throws EInvalidData, EAPIError, EAccessDenied {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("name", name);
		params.put(hash_type, hash);
		params.put("output_type", getOutputType());
		for (String key : _output_params.keySet()) {
			params.put(OUTPUT_PARAMS_PREFIX + key, _output_params.get(key));
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
