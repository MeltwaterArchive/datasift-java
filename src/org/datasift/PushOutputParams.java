package org.datasift;

import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

public class PushOutputParams extends HashMap<String, String> {
	private static final long serialVersionUID = 1L;
	
	public PushOutputParams() {
		
	}

	public PushOutputParams(JSONObject json) throws JSONException {
		parse(json);
	}
	
	public void set(String key, String val) {
		put(key, val);
	}
	
	public void parse(JSONObject json) throws JSONException {
		setOutputParams(json, "");
	}

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
