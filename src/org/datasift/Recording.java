/**
 * This file contains the Recording class.
 */
package org.datasift;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author MediaSift
 * @version 0.1
 */
public class Recording {

	protected User _user = null;
	protected String _id = "";
	protected Integer _start_time = null;
	protected Integer _end_time = null;
	protected String _name = null;
	protected String _hash = null;
	protected boolean _deleted = false;

	/**
	 * Construct a Recording object by fetching data from the DataSift API.
	 * 
	 * @param user
	 * @param id
	 * @throws EAccessDenied 
	 * @throws EAPIError 
	 * @throws EInvalidData 
	 */
	public Recording(User user, String id) throws EInvalidData, EAPIError, EAccessDenied {
		_user = user;
		_id = id;
		reloadData();
	}

	/**
	 * Construct a Recording object from a JSON object.
	 * 
	 * @param user
	 * @param data
	 * @throws EInvalidData
	 */
	public Recording(User user, JSONdn data) throws EInvalidData {
		_user = user;
		init(data);
	}

	/**
	 * Initialise this object with data from a JSON object.
	 * 
	 * @param user
	 * @param data
	 * @throws EInvalidData 
	 */
	public void init(JSONdn data) throws EInvalidData {
		String id = data.getStringVal("id");
		Integer start_time = data.getIntVal("start_time");
		Integer end_time = 0;
		try {
			end_time = data.getIntVal("finish_time");
		} catch (EInvalidData e) {
			// This means the finish time is either missing or null, which is acceptable.
			end_time = 0;
		}
		String name = data.getStringVal("name");
		String hash = data.getStringVal("hash");

		// Validate the data
		validateData(id, start_time, end_time, name, hash);

		// Initialise the object's data
		_id = id;
		_start_time = start_time;
		_end_time = end_time;
		_name = name;
		_hash = hash;
	}
	
	/**
	 * Validate a set of data for a recording.
	 * 
	 * @param id
	 * @param start_time
	 * @param end_time
	 * @param name
	 * @param hash
	 * @throws EInvalidData 
	 */
	public void validateData(String id, Integer start_time, Integer end_time, String name, String hash) throws EInvalidData {
		if (id != null && id.length() == 0) {
			throw new EInvalidData("Invalid ID in the recording data.");
		}
		
		if (start_time != null && start_time < 1) {
			throw new EInvalidData("Invalid start time in the recording data.");
		}
		
		if (end_time != null && end_time < 0) {
			throw new EInvalidData("Invalid end time in the recording data.");
		}

		if (name != null && name.length() == 0) {
			throw new EInvalidData("Invalid name in the recording data.");
		}
		
		if (hash != null && hash.length() == 0) {
			throw new EInvalidData("Invalid hash in the recording data.");
		}
	}
	
	/**
	 * Fetch the data for this recording from the DataSift API.
	 * 
	 * @throws EAccessDenied 
	 * @throws EAPIError 
	 * @throws EInvalidData 
	 */
	protected void reloadData() throws EInvalidData, EAPIError, EAccessDenied {
		// Get the data for this recording from the API
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("id", _id);
		try {
			init(new JSONdn(_user.callAPI("recording", params).toString()));
		} catch (JSONException e) {
			throw new EAPIError("Failed to parse the response from the DataSift API");
		}
	}
	
	/**
	 * Throw an exception if this recording has been deleted.
	 * 
	 * @throws EInvalidData 
	 */
	protected void checkDeleted() throws EInvalidData {
		if (_deleted) {
			throw new EInvalidData("This recording has been deleted");
		}
	}

	/**
	 * Return the recording ID.
	 * 
	 * @return
	 * @throws EInvalidData 
	 */
	public String getID() throws EInvalidData {
		checkDeleted();
		return _id;
	}

	/**
	 * Return the recording start time.
	 * 
	 * @return
	 * @throws EInvalidData 
	 */
	public Integer getStartTime() throws EInvalidData {
		checkDeleted();
		return _start_time;
	}

	/**
	 * Return the recording end time.
	 * 
	 * @return
	 * @throws EInvalidData 
	 */
	public Integer getEndTime() throws EInvalidData {
		checkDeleted();
		return _end_time;
	}

	/**
	 * Return the recording name.
	 * 
	 * @return
	 * @throws EInvalidData 
	 */
	public String getName() throws EInvalidData {
		checkDeleted();
		return _name;
	}

	/**
	 * Return the recording hash.
	 * 
	 * @return
	 * @throws EInvalidData 
	 */
	public String getHash() throws EInvalidData {
		checkDeleted();
		return _hash;
	}
	
	/**
	 * Update the recording data. Pass null for variables you don't want to change.
	 * @param id
	 * @param start_time
	 * @param end_time
	 * @param name
	 * @param hash
	 * @throws EInvalidData 
	 * @throws EAccessDenied 
	 * @throws EAPIError 
	 */
	public void update(Integer start_time, Integer end_time, String name) throws EInvalidData, EAPIError, EAccessDenied {
		checkDeleted();
		
		HashMap<String, String> params = new HashMap<String, String>();
		
		validateData(null, start_time, end_time, name, null);
		
		if (start_time != null) {
			params.put("start", String.valueOf(start_time));
		}
		
		if (end_time != null) {
			params.put("end", String.valueOf(end_time));
		}
		
		if (name != null) {
			params.put("name", name);
		}
		
		params.put("id", _id);
		
		// Call the API and re-initialise this object with the returned data
		try {
			init(new JSONdn(_user.callAPI("recording/update", params).toString()));
		} catch (JSONException e) {
			throw new EAPIError("Failed to parse the respnse from the DataSift API");
		}
	}

	/**
	 * Delete this recording.
	 * 
	 * @throws EInvalidData 
	 * @throws EAccessDenied 
	 * @throws EAPIError 
	 */
	public void delete() throws EInvalidData, EAPIError, EAccessDenied {
		checkDeleted();
		
		HashMap<String, String> params = new HashMap<String, String>();
		
		params.put("id", _id);
		
		JSONObject res = _user.callAPI("recording/delete", params);
		
		try {
			if (res.getString("success") != "true") {
				throw new EAPIError("Delete operation failed");
			}
		} catch (JSONException e) {
			throw new EAPIError("Delete operation failed");
		}
		
		_deleted = true;
	}
	
	/**
	 * Start a new export of the data contained within this recording.
	 * 
	 * @param format
	 * @return
	 * @throws EInvalidData 
	 * @throws EAccessDenied 
	 * @throws EAPIError 
	 */
	public RecordingExport startExport(String format) throws EInvalidData, EAPIError, EAccessDenied {
		return startExport(format, null, null, null);
	}
	
	/**
	 * Start a new export of the data contained within this recording.
	 * 
	 * @param format
	 * @param start
	 * @return
	 * @throws EInvalidData 
	 * @throws EAccessDenied 
	 * @throws EAPIError 
	 */
	public RecordingExport startExport(String format, int start) throws EInvalidData, EAPIError, EAccessDenied {
		return startExport(format, null, start, null);
	}

	/**
	 * Start a new export of the data contained within this recording.
	 * 
	 * @param format
	 * @param name
	 * @param start
	 * @param end
	 * @return
	 * @throws EInvalidData 
	 * @throws EAccessDenied 
	 * @throws EAPIError 
	 */
	public RecordingExport startExport(String format, String name, Integer start, Integer end) throws EInvalidData, EAPIError, EAccessDenied {
		checkDeleted();
		
		HashMap<String, String> params = new HashMap<String, String>();
		
		if (format != "json" && format != "xls" && format != "xlsx") {
			throw new EInvalidData("Invalid export format specified");
		}
		
		params.put("format", format);
		
		if (name != null) {
			if (name.length() > 0) {
				params.put("name", name);
			}
		}
		
		if (start != null) {
			if (start < _start_time || start > _end_time) {
				throw new EInvalidData("The start timestamp must be within the recorded time period");
			}
			params.put("start", String.valueOf(start));
		}
		
		if (end != null) {
			if (end < _start_time || end > _end_time) {
				throw new EInvalidData("The end time must be within the recorded time period");
			}
			if (start != null && end < start) {
				throw new EInvalidData("The start timestamp must be earlier than the end timestamp");
			}
			params.put("end", String.valueOf(end));
		}
		
		try {
			return new RecordingExport(_user, new JSONdn(_user.callAPI("recording/export/start", params).toString()));
		} catch (JSONException e) {
			throw new EAPIError("Failed to parse the response from the DataSift API");
		}
	}
}
