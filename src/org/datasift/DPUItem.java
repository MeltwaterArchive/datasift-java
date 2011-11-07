/**
 * This file contains the CostItem class which represents an individual item
 * within a cost structure.
 * @see DPU
 */
package org.datasift; 

import java.util.HashMap;


/**
 * @author MediaSift
 * @version 0.1
 */
public class DPUItem {

	private int _count = 0;
	private double _dpu = 0;
	private HashMap<String, DPUItem> _targets = null;

	public DPUItem(int count, double d) {
		_count = count;
		_dpu = d;
		_targets = new HashMap<String,DPUItem>();
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
	public double getDPU() {
		return _dpu;
	}

	/**
	 * @return the _targets
	 */
	public HashMap<String, DPUItem> getTargets() {
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
	 * @param dpuItem
	 */
	public void addTarget(String target, DPUItem dpuItem) {
		_targets.put(target, dpuItem);
	}
}
