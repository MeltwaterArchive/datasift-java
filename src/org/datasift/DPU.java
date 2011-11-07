/**
 * 
 */
package org.datasift;

import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author stuart
 *
 */
public class DPU extends JSONdn {

	private HashMap<String,DPUItem> _dpu = null;
	
	private double _total = 0;

	/**
	 * @param source
	 * @throws EInvalidData 
	 * @throws JSONException 
	 */
	public DPU(String source) throws EInvalidData, JSONException {
		super(source);
		
		// Iterate over the DPU items
		_dpu = new HashMap<String,DPUItem>();

		JSONObject c = getJSONObjectVal("detail");
		Iterator<?> dpukeys = c.keys();
		while (dpukeys.hasNext()) {
			// Create a new CostItem
			String key = (String) dpukeys.next();
			DPUItem item = new DPUItem(getIntVal("detail." + key + ".count"), getDoubleVal("detail." + key + ".dpu"));
			
			// Iterate over the cost item targets, adding them to the new CostItem's targets
			JSONObject t = getJSONObjectVal("detail." + key + ".targets");
			Iterator<?> targets = t.keys();
			while (targets.hasNext()) {
				String targetkey = (String) targets.next();
				JSONObject t2 = t.getJSONObject(targetkey);
				item.addTarget(targetkey, new DPUItem(t2.getInt("count"), t2.getDouble("dpu")));
			}

			// Add the new DPUItem to the dpu map
			_dpu.put(key, item);
		}
		
		_total = getDoubleVal("dpu");
	}

	/**
	 * @return the _total
	 */
	public double getTotal() {
		return _total;
	}
	
	/**
	 * @return the _dpu
	 */
	public HashMap<String,DPUItem> getDPU() {
		return _dpu;
	}

}
