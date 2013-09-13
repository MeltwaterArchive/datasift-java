/**
 * This file contains the JSONdn class and provides dot-notation access to a
 * JSON source.
 */
package org.datasift;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author MediaSift
 * @version 0.1
 */
public class JSONdn extends JSONObject {

	/**
	 * Constructor. Takes a JSON string.
	 * 
	 * @param source
	 * @throws JSONException
	 */
	public JSONdn(String source) throws JSONException {
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
	 * Get a JSONObject value.
	 * @throws EInvalidData
	 * @see org.json.JSONObject#getJSONObject(java.lang.String)
	 */
	public JSONObject getJSONObjectVal(String key) throws EInvalidData {
		try {
			int pos = key.lastIndexOf('.');
			if (pos != -1) {
				JSONObject obj = resolveString(key);
				return obj.getJSONObject(key.substring(pos + 1));
			}
			return super.getJSONObject(key);
		} catch (JSONException e) {
			throw new EInvalidData("Requested key (\"" + key
					+ "\") does not exist");
		}
	}

	/**
	 * Get a boolean value.
	 * @throws EInvalidData
	 * @see org.json.JSONObject#getBoolean(java.lang.String)
	 */
	public boolean getBooleanVal(String key) throws EInvalidData {
		try {
			int pos = key.lastIndexOf('.');
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
	 * Get a boolean value or falls back to the default provided if not found
	 * @param key
	 * @param defaultVal
	 * @return
	 */
	public boolean getBooleanVal(String key, boolean defaultVal) {
		try {
			return (this.has(key)) ? this.getBooleanVal(key) : defaultVal;
		} catch (EInvalidData e) {
			throw new RuntimeException("Key (\""+key+"\") allegedly present but value not found");
		}
	}

	/**
	 * Get a double value.
	 * @throws EInvalidData
	 * @see org.json.JSONObject#getDouble(java.lang.String)
	 */
	public double getDoubleVal(String key) throws EInvalidData {
		try {
			int pos = key.lastIndexOf('.');
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
	 * Get a double value or falls back to the default provided if not found
	 * @param key
	 * @param defaultVal
	 * @return
	 */
	public double getDoubleVal(String key, double defaultVal) {
		try {
			return (this.has(key)) ? this.getDoubleVal(key) : defaultVal;
		} catch (EInvalidData e) {
			throw new RuntimeException("Key (\""+key+"\") allegedly present but value not found");
		}
	}

	/**
	 * Get an integer value.
	 * @throws EInvalidData
	 * @see org.json.JSONObject#getInt(java.lang.String)
	 */
	public int getIntVal(String key) throws EInvalidData {
		try {
			int pos = key.lastIndexOf('.');
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
	 * Get a int value or falls back to the default provided if not found
	 * @param key
	 * @param defaultVal
	 * @return
	 */
	public int getIntVal(String key, int defaultVal) {
		try {
			return (this.has(key)) ? this.getIntVal(key) : defaultVal;
		} catch (EInvalidData e) {
			throw new RuntimeException("Key (\""+key+"\") allegedly present but value not found");
		}
	}

	/**
	 * Get a long value.
	 * @throws EInvalidData
	 * @see org.json.JSONObject#getLong(java.lang.String)
	 */
	public long getLongVal(String key) throws EInvalidData {
		try {
			int pos = key.lastIndexOf('.');
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
	 * Get a long value or falls back to the default provided if not found
	 * @param key
	 * @param defaultVal
	 * @return
	 */
	public long getLongVal(String key, long defaultVal) {
		try {
			return (this.has(key)) ? this.getLongVal(key) : defaultVal;
		} catch (EInvalidData e) {
			throw new RuntimeException("Key (\""+key+"\") allegedly present but value not found");
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
	 * Get a String value or falls back to the default provided if not found
	 * @param key
	 * @param defaultVal
	 * @return
	 */
	public String getStringVal(String key, String defaultVal) {
		try {
			return (this.has(key)) ? this.getStringVal(key) : defaultVal;
		} catch (EInvalidData e) {
			throw new RuntimeException("Key (\""+key+"\") allegedly present but value not found");
		}
	}

	/**
	 * Check whether a value exists.
	 * @see org.json.JSONObject#has(java.lang.String)
	 * @param key
	 */
	@Override
	public boolean has(String key) {
		int pos = key.lastIndexOf('.');
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
