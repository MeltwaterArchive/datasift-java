package org.datasift.pushsubscription;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Log implements Iterable<LogEntry> {
	private int _total_count = 0;
	private ArrayList<LogEntry> _log_entries = new ArrayList<LogEntry>();
	
	public Log(JSONObject json) throws JSONException {
		_total_count = json.getInt("count");
        JSONArray log_entries = json.getJSONArray("log_entries");
        for (int i = 0; i < log_entries.length(); i++) {
            _log_entries.add(new LogEntry(log_entries.getJSONObject(i)));
        }
	}
	
	public int getCount() {
		return _log_entries.size();
	}

	public int getTotalCount() {
		return _total_count;
	}
	
	public ArrayList<LogEntry> getEntries() {
		return _log_entries;
	}

	@Override
	public Iterator<LogEntry> iterator() {
		return _log_entries.iterator();
	}
}
