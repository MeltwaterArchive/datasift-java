/**
 * This file contains the Usage class.
 */
package org.datasift;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONException;

/**
 * The Usage class represents data returned by the /usage API endpoint. It is
 * either the overall data, with detailed items for each stream, or it's the
 * data for an individual stream with detailed items for each interaction type.
 * 
 * @author MediaSift
 */
public class Usage extends JSONdn {

	protected String _items_key = "streams";

	/**
	 * @param source
	 * @throws EInvalidData
	 * @throws JSONException
	 */
	public Usage(String source) throws EInvalidData, JSONException {
		super(source);

		try {
			getJSONObject("streams");
		} catch (JSONException e) {
			_items_key = "types";
		}
	}

	/**
	 * Returns the total number of items processed.
	 * 
	 * @return
	 * @throws EInvalidData
	 */
	public int getProcessed() throws EInvalidData {
		return getIntVal("processed");
	}

	/**
	 * Returns the number of items processed for this stream or type.
	 * 
	 * @param hash
	 * @return
	 * @throws EInvalidData
	 */
	public int getProcessed(String item) throws EInvalidData {
		return getIntVal(_items_key + "." + item + ".processed");
	}

	/**
	 * Returns the total number of items delivered.
	 * 
	 * @return
	 * @throws EInvalidData
	 */
	public int getDelivered() throws EInvalidData {
		return getIntVal("delivered");
	}

	/**
	 * Returns the number of items delivered for this stream or type.
	 * 
	 * @param hash
	 * @return
	 * @throws EInvalidData
	 */
	public int getDelivered(String item) throws EInvalidData {
		return getIntVal(_items_key + "." + item + ".delivered");
	}

	/**
	 * Return the streams or types for which we have detailed data.
	 * 
	 * @return
	 * @throws EInvalidData
	 */
	public String[] getItems() {
		ArrayList<String> retval = new ArrayList<String>();
		try {
			@SuppressWarnings("unchecked")
			Iterator<String> i = getJSONObject(_items_key).keys();
			while (i.hasNext()) {
				retval.add(i.next());
			}
		} catch (JSONException e) {
			// Items key not found, ignore this and return an empty array
		}
		return retval.toArray(new String[0]);
	}
}
