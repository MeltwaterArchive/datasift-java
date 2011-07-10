/**
 * This file contains the CostItem class which represents an individual item
 * within a cost structure.
 * @see Cost
 */
package org.datasift; 

import java.util.HashMap;


/**
 * @author MediaSift
 * @version 0.1
 */
public class CostItem {

	private int _count = 0;
	private int _cost = 0;
	private HashMap<String, CostItem> _targets = null;

	public CostItem(int count, int cost) {
		_count = count;
		_cost = cost;
		_targets = new HashMap<String,CostItem>();
	}

	/**
	 * @return the _count
	 */
	public int getCount() {
		return _count;
	}

	/**
	 * @return the _cost
	 */
	public int getCost() {
		return _cost;
	}

	/**
	 * @return the _targets
	 */
	public HashMap<String, CostItem> getTargets() {
		return _targets;
	}
	
	/**
	 * @return true if this object has any targets
	 */
	public boolean hasTargets() {
		return (_targets.size() > 0);
	}

	/**
	 * Add a CostItem to the targets hash.
	 * @param target
	 * @param costItem
	 */
	public void addTarget(String target, CostItem costItem) {
		_targets.put(target, costItem);
	}
}
