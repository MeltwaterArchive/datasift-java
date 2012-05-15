/**
 * This file contains the Usage class.
 */
package org.datasift;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

	private DateFormat _df = null;

	/**
	 * @param source
	 * @throws EInvalidData
	 * @throws JSONException
	 */
	public Usage(String source) throws EInvalidData, JSONException {
		super(source);
		_df = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss Z");
	}

	/**
	 * Returns a Date object representing the start date.
	 * 
	 * @return
	 * @throws EInvalidData
	 * @throws ParseException 
	 */
	public Date getStartDate() throws EInvalidData, ParseException {
		return _df.parse(getStringVal("start"));
	}

	/**
	 * Returns a Date object representing the start date.
	 * 
	 * @return
	 * @throws EInvalidData
	 * @throws ParseException 
	 */
	public Date getEndDate() throws EInvalidData, ParseException {
		return _df.parse(getStringVal("end"));
	}

	/**
	 * Return the streams or types for which we have detailed data.
	 * 
	 * @return
	 * @throws EInvalidData
	 */
	public String[] getStreamHashes() {
		ArrayList<String> retval = new ArrayList<String>();
		try {
			@SuppressWarnings("unchecked")
			Iterator<String> i = getJSONObject("streams").keys();
			while (i.hasNext()) {
				retval.add(i.next());
			}
		} catch (JSONException e) {
			// Streams key not found, ignore this and return an empty array
		}
		return retval.toArray(new String[0]);
	}

	/**
	 * Returns the number of seconds for which a stream has been consumed.
	 * 
	 * @param hash
	 * @return
	 * @throws EInvalidData
	 */
	public int getSeconds(String stream_hash) throws EInvalidData {
		return getIntVal("streams." + stream_hash + ".seconds");
	}

	/**
	 * Return the streams or types for which we have detailed data.
	 * 
	 * @return
	 * @throws EInvalidData
	 */
	public String[] getLicenseTypes(String stream_hash) {
		ArrayList<String> retval = new ArrayList<String>();
		try {
			@SuppressWarnings("unchecked")
			Iterator<String> i = getJSONObjectVal("streams." + stream_hash + ".licenses").keys();
			while (i.hasNext()) {
				retval.add(i.next());
			}
		} catch (EInvalidData e) {
			// Licenses key not found, ignore this and return an empty array
		}
		return retval.toArray(new String[0]);
	}

	/**
	 * Returns the number of seconds for which a stream has been consumed.
	 * 
	 * @param hash
	 * @return
	 * @throws EInvalidData
	 */
	public int getLicenseUsage(String stream_hash, String type) throws EInvalidData {
		return getIntVal("streams." + stream_hash + ".licenses." + type);
	}
}
