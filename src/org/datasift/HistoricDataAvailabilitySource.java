package org.datasift;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HistoricDataAvailabilitySource {
	private int _status = 0;
	private ArrayList<Integer> _versions = null;
	private HashMap<String,Integer> _augmentations = null;
	
	public HistoricDataAvailabilitySource(String json) throws JSONException {
		this(new JSONObject(json));
	}
	
	public HistoricDataAvailabilitySource(JSONObject json) throws JSONException {
		_status = json.getInt("status");
				
		_versions = new ArrayList<Integer>();
		JSONArray versions = json.getJSONArray("versions");
        for (int i = 0; i < versions.length(); i++) {
            _versions.add(versions.getInt(i));
        }

		_augmentations = new HashMap<String,Integer>();
		JSONObject augmentations = json.getJSONObject("augmentations");
		Iterator<?> augmentationkeys = augmentations.keys();
		while (augmentationkeys.hasNext()) {
			String key = (String) augmentationkeys.next();
			_augmentations.put(key, augmentations.getInt(key));
		}
	}
	
	public int getStatus() {
		return _status;
	}
	
	public ArrayList<Integer> getVersions() {
		return _versions;
	}
	
	public HashMap<String,Integer> getAugmentations() {
		return _augmentations;
	}
	
	public int getAugmentation(String augmentation) {
		if (_augmentations.containsKey(augmentation)) {
			return _augmentations.get(augmentation);
		}
		return 0;
	}
}
