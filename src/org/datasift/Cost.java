/**
 * This file contains the Cost class.
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
public class Cost extends JSONdn {

	private HashMap<String,CostItem> _costs = null;
	
	private int _totalCost = 0;

	/**
	 * @param source
	 * @throws EInvalidData 
	 * @throws JSONException 
	 */
	public Cost(String source) throws EInvalidData, JSONException {
		super(source);
		
		// Iterate over the cost items
		_costs = new HashMap<String,CostItem>();

		JSONObject c = getJSONObjectVal("costs");
		Iterator<?> costkeys = c.keys();
		while (costkeys.hasNext()) {
			// Create a new CostItem
			String key = (String) costkeys.next();
			CostItem item = new CostItem(getIntVal("costs." + key + ".count"), getIntVal("costs." + key + ".cost"));
			
			// Iterate over the cost item targets, adding them to the new CostItem's targets
			JSONObject t = getJSONObjectVal("costs." + key + ".targets");
			Iterator<?> targets = t.keys();
			while (targets.hasNext()) {
				String targetkey = (String) targets.next();
				JSONObject t2 = t.getJSONObject(targetkey);
				item.addTarget(targetkey, new CostItem(t2.getInt("count"), t2.getInt("cost")));
			}

			// Add the new CostItem to the costs map
			_costs.put(key, item);
		}
		
		_totalCost = getIntVal("total");
	}

	/**
	 * @return the _totalCost
	 */
	public int getTotalCost() {
		return _totalCost;
	}
	
	/**
	 * @return the costs
	 */
	public HashMap<String,CostItem> getCosts() {
		return _costs;
	}

}
