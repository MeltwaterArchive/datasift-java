/**
 * This file contains the Interaction class and provides access to an
 * interaction from a JSON source.
 */
package org.datasift;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author MediaSift
 * @version 0.1
 */
public class Interaction extends JSONObject {

	/**
	 * Constructor. Takes a JSON string.
	 * 
	 * @param source
	 * @throws JSONException
	 */
	public Interaction(String source) throws JSONException {
		super(source);
	}

	/**
	 * Walks down a .-delimited string to get to the JSONObject one level
	 * below the end.
	 * 
	 * @param str
	 * @return
	 * @throws JSONException
	 */
	public JSONObject resolveString(String str) throws JSONException {
		try {
			String[] parts = str.split("\\.");
			JSONObject retval = getJSONObject(parts[0]);
			for (int i = 1; i < parts.length - 1; i++) {
				retval = retval.getJSONObject(parts[i]);
			}
			return retval;
		} catch (JSONException e) {
			throw e;
		}
	}

	/**
	 * Get a boolean value.
	 * @throws EInvalidData
	 * @see org.json.JSONObject#getBoolean(java.lang.String)
	 */
	public boolean getBooleanVal(String key) throws EInvalidData {
		try {
			int pos = key.indexOf('.');
			if (pos != -1) {
				JSONObject obj = resolveString(key);
				return obj.getBoolean(key.substring(pos + 1));
			}
			return super.getBoolean(key);
		} catch (JSONException e) {
			throw new EInvalidData("Requested key (\"" + key
					+ "\") does not exist");
		}
	}

	/**
	 * Get a double value.
	 * @throws EInvalidData
	 * @see org.json.JSONObject#getDouble(java.lang.String)
	 */
	public double getDoubleVal(String key) throws EInvalidData {
		try {
			int pos = key.indexOf('.');
			if (pos != -1) {
				JSONObject obj = resolveString(key);
				return obj.getDouble(key.substring(pos + 1));
			}
			return super.getDouble(key);
		} catch (JSONException e) {
			throw new EInvalidData("Requested key (\"" + key
					+ "\") does not exist");
		}
	}

	/**
	 * Get an integer value.
	 * @throws EInvalidData
	 * @see org.json.JSONObject#getInt(java.lang.String)
	 */
	public int getIntVal(String key) throws EInvalidData {
		try {
			int pos = key.indexOf('.');
			if (pos != -1) {
				JSONObject obj = resolveString(key);
				return obj.getInt(key.substring(pos + 1));
			}
			return super.getInt(key);
		} catch (JSONException e) {
			throw new EInvalidData("Requested key (\"" + key
					+ "\") does not exist");
		}
	}

	/**
	 * Get a long value.
	 * @throws EInvalidData
	 * @see org.json.JSONObject#getLong(java.lang.String)
	 */
	public long getLongVal(String key) throws EInvalidData {
		try {
			int pos = key.indexOf('.');
			if (pos != -1) {
				JSONObject obj = resolveString(key);
				return obj.getLong(key.substring(pos + 1));
			}
			return super.getLong(key);
		} catch (JSONException e) {
			throw new EInvalidData("Requested key (\"" + key
					+ "\") does not exist");
		}
	}

	/**
	 * Get a string value.
	 * @throws EInvalidData
	 * @see org.json.JSONObject#getString(java.lang.String)
	 */
	public String getStringVal(String key) throws EInvalidData {
		try {
			int pos = key.lastIndexOf('.');
			if (pos != -1) {
				JSONObject obj = resolveString(key);
				return obj.getString(key.substring(pos + 1));
			}
			return super.getString(key);
		} catch (JSONException e) {
			throw new EInvalidData("Requested key (\"" + key
					+ "\") does not exist");
		}
	}

	/**
	 * Check whether a value exists.
	 * @see org.json.JSONObject#has(java.lang.String)
	 * @param key
	 */
	@Override
	public boolean has(String key) {
		int pos = key.indexOf('.');
		if (pos != -1) {
			JSONObject obj;
			try {
				obj = resolveString(key);
			} catch (JSONException e) {
				return false;
			}
			return obj.has(key.substring(pos + 1));
		}
		return super.has(key);
	}
}
