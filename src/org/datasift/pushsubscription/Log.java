/**
 * This file contains the Log class.
 */
package org.datasift.pushsubscription;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * The Log class represents a collection of LogEntry objects.
 * 
 * @author MediaSift
 * @version 0.1
 */
public class Log implements Iterable<LogEntry> {
	/**
	 * @var int The total number of log entries that matched the request sent
	 *          to the API (for use when paging).
	 */
	private int _total_count = 0;
	
	/**
	 * @var ArrayList<LogEntry> The log entries.
	 */
	private ArrayList<LogEntry> _log_entries = new ArrayList<LogEntry>();
	
	/**
	 * Construct the object from a JSONObject API response.
	 * 
	 * @param JSONObject json The JSONObject containing the response.
	 * @throws JSONException
	 */
	public Log(JSONObject json) throws JSONException {
		_total_count = json.getInt("count");
        JSONArray log_entries = json.getJSONArray("log_entries");
        for (int i = 0; i < log_entries.length(); i++) {
            _log_entries.add(new LogEntry(log_entries.getJSONObject(i)));
        }
	}
	
	/**
	 * Get the number of LogEntry objects in this Log.
	 * @return int
	 */
	public int getCount() {
		return _log_entries.size();
	}

	/**
	 * Get the total number of matchin log entries as reported in the API
	 * response.
	 * @return int
	 */
	public int getTotalCount() {
		return _total_count;
	}
	
	/**
	 * Get all of the LogEntry objects.
	 * @return ArrayList<LogEntry>
	 */
	public ArrayList<LogEntry> getEntries() {
		return _log_entries;
	}

	/**
	 * Get an iterator for the LogEntry objects contained within this Log
	 * object.
	 * @return Iterator<LogEntry>
	 */
	@Override
	public Iterator<LogEntry> iterator() {
		return _log_entries.iterator();
	}
}
