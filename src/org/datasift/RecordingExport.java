/**
 * This file contains the RecordingExport class.
 */
package org.datasift;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author MediaSift
 * @version 0.1
 */
public class RecordingExport {

	protected User _user = null;
	protected String _id = "";
	protected String _recording_id = "";
	protected String _name = null;
	protected long _start = 0;
	protected long _end = 0;
	protected String _status = "unknown";
	protected boolean _deleted = false;

	/**
	 * Construct a RecordingExport object by fetching data from the DataSift API.
	 * 
	 * @param user
	 * @param id
	 * @throws EAccessDenied 
	 * @throws EAPIError 
	 * @throws EInvalidData 
	 */
	public RecordingExport(User user, String id) throws EInvalidData, EAPIError, EAccessDenied {
		_user = user;
		_id = id;
		reloadData();
	}

	/**
	 * Construct a RecordingExport object from a JSON object.
	 * 
	 * @param user
	 * @param data
	 * @throws EInvalidData
	 */
	public RecordingExport(User user, JSONdn data) throws EInvalidData {
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
		// The export ID
		String id = data.getStringVal("id");
		// The recording ID
		String recording_id = data.getStringVal("recording_id");
		// The start time (optional)
		long start = 0;
		try {
			start = data.getLongVal("start");
		} catch (EInvalidData e) {
			// This means the start time is either missing or null, which is acceptable.
		}
		// The end time (optional)
		long end = 0;
		try {
			end = data.getLongVal("end");
		} catch (EInvalidData e) {
			// This means the finish time is either missing or null, which is acceptable.
		}
		// The recording name
		String name = data.getStringVal("name");
		// The recording status
		String status = "unknown";
		try {
			status = data.getStringVal("status");
		} catch (EInvalidData e) {
			// This means the status is either missing or null, which is acceptable.
		}

		// Validate the data
		validateData(id, recording_id, start, end, name, status);

		// Initialise the object's data
		_id = id;
		_recording_id = recording_id;
		_start = start;
		_end = end;
		_name = name;
		_status = status;
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
	public void validateData(String id, String recording_id, long start, long end, String name, String status) throws EInvalidData {
		if (id != null && id.length() == 0) {
			throw new EInvalidData("Invalid ID in the export data.");
		}
		
		if (recording_id != null && recording_id.length() == 0) {
			throw new EInvalidData("Invalid recording ID in the export data.");
		}
		
		if (start < 1) {
			throw new EInvalidData("Invalid start time in the export data.");
		}
		
		if (end < 1) {
			throw new EInvalidData("Invalid end time in the export data.");
		}

		if (name != null && name.length() == 0) {
			throw new EInvalidData("Invalid name in the export data.");
		}
		
		if (status != null && status.length() == 0) {
			throw new EInvalidData("Invalid status in the export data.");
		}
	}
	
	/**
	 * Fetch the data for this export from the DataSift API.
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
			init(new JSONdn(_user.callAPI("recording/export", params).toString()));
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
			throw new EInvalidData("This export has been deleted");
		}
	}

	/**
	 * Return the export ID.
	 * 
	 * @return
	 * @throws EInvalidData 
	 */
	public String getID() throws EInvalidData {
		checkDeleted();
		return _id;
	}

	/**
	 * Return the recording ID.
	 * 
	 * @return
	 * @throws EInvalidData 
	 */
	public String getRecordingID() throws EInvalidData {
		checkDeleted();
		return _recording_id;
	}

	/**
	 * Return the export start time.
	 * 
	 * @return
	 * @throws EInvalidData 
	 */
	public long getStart() throws EInvalidData {
		checkDeleted();
		return _start;
	}

	/**
	 * Return the export end time.
	 * 
	 * @return
	 * @throws EInvalidData 
	 */
	public long getEnd() throws EInvalidData {
		checkDeleted();
		return _end;
	}

	/**
	 * Return the export name.
	 * 
	 * @return
	 * @throws EInvalidData 
	 */
	public String getName() throws EInvalidData {
		checkDeleted();
		return _name;
	}

	/**
	 * Return the export status.
	 * 
	 * @return
	 * @throws EInvalidData 
	 */
	public String getStatus() throws EInvalidData {
		checkDeleted();
		return _status;
	}

	/**
	 * Delete this export.
	 * 
	 * @throws EInvalidData 
	 * @throws EAccessDenied 
	 * @throws EAPIError 
	 */
	public void delete() throws EInvalidData, EAPIError, EAccessDenied {
		checkDeleted();
		
		HashMap<String, String> params = new HashMap<String, String>();
		
		params.put("id", _id);
		
		JSONObject res = _user.callAPI("recording/export/delete", params);
		
		try {
			if (res.getString("success") != "true") {
				throw new EAPIError("Delete operation failed");
			}
		} catch (JSONException e) {
			throw new EAPIError("Delete operation failed");
		}
		
		_deleted = true;
	}
}
