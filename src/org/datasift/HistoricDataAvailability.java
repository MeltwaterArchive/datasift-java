package org.datasift;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

public class HistoricDataAvailability {
	private Date _start = null;
	private Date _end = null;
	private HashMap<String,HistoricDataAvailabilitySource> _sources = null;
	
	public HistoricDataAvailability(String json) throws EInvalidData, JSONException {
		this(new JSONObject(json));
	}
	
	public HistoricDataAvailability(JSONObject json) throws EInvalidData, JSONException {
		_start = new Date(json.getLong("start") * 1000);
		_end = new Date(json.getLong("end") * 1000);
		
		_sources = new HashMap<String,HistoricDataAvailabilitySource>();
		JSONObject sources = null;
		try {
			sources = json.getJSONObject("sources");
		} catch (JSONException e) {
			sources = null;
		}

		if (sources != null) {
			Iterator<?> sourcekeys = sources.keys();
			while (sourcekeys.hasNext()) {
				String key = (String) sourcekeys.next();
				_sources.put(key, new HistoricDataAvailabilitySource(sources.getJSONObject(key)));
			}
		}
	}
	
	public Date getStartDate() {
		return _start;
	}
	
	public Date getEndDate() {
		return _end;
	}
	
	public HashMap<String,HistoricDataAvailabilitySource> getSources() {
		return _sources;
	}
	
	public HistoricDataAvailabilitySource getSource(String source) {
		return _sources.get(source);
	}
}
