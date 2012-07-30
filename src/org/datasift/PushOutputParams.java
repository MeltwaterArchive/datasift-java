/**
 * This file contains the PushOutputParams class.
 */
package org.datasift;

import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * The PushOutputParams class represent a set of output parameters.
 * 
 * @author MediaSift
 * @version 0.1
 */
public class PushOutputParams extends HashMap<String, String> {
	/**
	 * Serialisation ID.
	 * @var long
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor.
	 */
	public PushOutputParams() {
		
	}

	/**
	 * Construct an object from parameters in a JSONObject.
	 * 
	 * @param JSONObject json The parameters to parse.
	 * @throws JSONException
	 */
	public PushOutputParams(JSONObject json) throws JSONException {
		parse(json);
	}
	
	/**
	 * Set an output parameter.
	 * 
	 * @param String key The parameter to set.
	 * @param String val The value to set.
	 */
	public void set(String key, String val) {
		put(key, val);
	}
	
	/**
	 * Parse a JSONObject into this object.
	 * 
	 * @param JSONObject json The object to parse.
	 * @throws JSONException
	 */
	public void parse(JSONObject json) throws JSONException {
		setOutputParams(json, "");
	}

	/**
	 * Recursive method to parse an arbitrarily deep set of nested parameters.
	 * 
	 * @param JSONObject output_params The object to parse.
	 * @param String     prefix        The current prefix.
	 * @throws JSONException
	 */
	protected void setOutputParams(JSONObject output_params, String prefix) throws JSONException {
		Iterator<?> keys = output_params.keys();
		while (keys.hasNext()) {
			// Create a new CostItem
			String key = (String) keys.next();
			JSONObject nested = output_params.optJSONObject(key);
			if (nested != null) {
				setOutputParams(nested, (prefix.length() == 0 ? "" : prefix + ".") + key);
			} else {
				set((prefix.length() == 0 ? "" : prefix + ".") + key, output_params.getString(key));
			}
		}
	}
}
