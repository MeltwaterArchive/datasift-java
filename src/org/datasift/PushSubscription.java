/**
 * This file contains the Destination class.
 */
package org.datasift;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.datasift.pushsubscription.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * The Destination class represents a data Destination.
 * 
 * @author MediaSift
 * @version 0.1
 */
public class PushSubscription extends PushDefinition {
	/**
	 * Auto-generated serialization version UID.
	 */
	private static final long serialVersionUID = -8162860993374079377L;
	
	/**
	 * Hash type constants.
	 */
	public final static String HASH_TYPE_STREAM   = "stream";
	public final static String HASH_TYPE_HISTORIC = "historic";
	
	/**
	 * Status constants.
	 */
	public final static String STATUS_ACTIVE    = "active";
	public final static String STATUS_PAUSED    = "paused";
	public final static String STATUS_STOPPED   = "stopped";
	public final static String STATUS_FINISHING = "finishing";
	public final static String STATUS_FINISHED  = "finished";
	public final static String STATUS_FAILED    = "failed";
	public final static String STATUS_DELETED   = "deleted";
	
	/**
	 * Order by constants.
	 */
	public final static String ORDERBY_ID = "id";
	public final static String ORDERBY_CREATED_AT = "created_at";
	public final static String ORDERBY_REQUEST_TIME = "request_time";
	
	/**
	 * Order direction constants.
	 */
	public final static String ORDERDIR_ASC = "asc";
	public final static String ORDERDIR_DESC = "desc";
	
	/**
	 * Get a push subscription by ID.
	 * 
	 * @param User   user The user who owns the subscription.
	 * @param String id   The subscription ID.
	 * @return PushSubscription
	 * @throws EAPIError
	 * @throws EAccessDenied
	 * @throws EInvalidData
	 */
	static public PushSubscription get(User user, String id) throws EAPIError, EAccessDenied, EInvalidData {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("id", id);
		return new PushSubscription(user, user.callAPI("push/get", params));
	}

	/**
	 * Get a list of push subscriptions in the given user's account. Limited
	 * to 100 results. Results will be returned in ascending order by creation
	 * date.
	 * 
	 * @param User user The user making the request.
	 * @return ArrayList<PushSubscription>
	 * @throws EInvalidData
	 * @throws EAPIError
	 * @throws EAccessDenied
	 */
	static public ArrayList<PushSubscription> list(User user) throws EInvalidData, EAPIError, EAccessDenied {
		return list(user, 1, 100);
	}
	
	/**
	 * Get a page of push subscriptions in the given user's account, where
	 * each page contains up to 20 items. Results will be returned in
	 * ascending order by creation date.
	 * 
	 * @param User user The user.
	 * @param int  page The page number to fetch.
	 * @return ArrayList<PushSubscription>
	 * @throws EInvalidData
	 * @throws EAPIError
	 * @throws EAccessDenied
	 */
	static public ArrayList<PushSubscription> list(User user, int page) throws EInvalidData, EAPIError, EAccessDenied {
		return list(user, page, 20);
	}
	
	/**
	 * Get a page of push subscriptions in the given user's account, where
	 * each page contains up to per_page items. Results will be returned in
	 * ascending order by creation date.
	 * 
	 * @param User user     The user.
	 * @param int  page     The page number to fetch.
	 * @param int  per_page The number of items per page.
	 * @return ArrayList<PushSubscription>
	 * @throws EInvalidData
	 * @throws EAPIError
	 * @throws EAccessDenied
	 */
	static public ArrayList<PushSubscription> list(User user, int page, int per_page) throws EInvalidData, EAPIError, EAccessDenied {
		return list(user, page, per_page, ORDERBY_CREATED_AT, ORDERDIR_ASC, false);
	}
	
	/**
	 * Get a page of push subscriptions in the given user's account, where
	 * each page contains up to per_page items. Results will be ordered
	 * according to the supplied ordering parameters.
	 * 
	 * @param User user                The user.
	 * @param int  page                The page number to fetch.
	 * @param int  per_page            The number of items per page.
	 * @param String order_by          The field on which to order the results.
	 * @param String order_dir         The direction of the ordering.
	 * @param boolean include_finished True to include subscriptions against
	 *                                 finished historic queries.
	 * @return ArrayList<PushSubscription>
	 * @throws EInvalidData
	 * @throws EAPIError
	 * @throws EAccessDenied
	 */
	static public ArrayList<PushSubscription> list(User user, int page, int per_page, String order_by, String order_dir, boolean include_finished) throws EInvalidData, EAPIError, EAccessDenied {
		return list(user, null, null, page, per_page, order_by, order_dir, include_finished);
	}
	
	/**
	 * Get a list of push subscriptions for the given stream hash. Limited
	 * to 100 results.Results will be returned in ascending order by creation
	 * date.
	 * 
	 * @param User user
	 * @return ArrayList<PushSubscription>
	 * @throws EInvalidData
	 * @throws EAPIError
	 * @throws EAccessDenied
	 */
	static public ArrayList<PushSubscription> listByStreamHash(User user, String hash) throws EInvalidData, EAPIError, EAccessDenied {
		return listByStreamHash(user, hash, 1, 100);
	}
	
	/**
	 * Get a page of push subscriptions for the given stream hash, where
	 * each page contains up to 20 items. Results will be returned in
	 * ascending order by creation date.
	 * 
	 * @param User user The user.
	 * @param int  page The page number to fetch.
	 * @return ArrayList<PushSubscription>
	 * @throws EInvalidData
	 * @throws EAPIError
	 * @throws EAccessDenied
	 */
	static public ArrayList<PushSubscription> listByStreamHash(User user, String hash, int page) throws EInvalidData, EAPIError, EAccessDenied {
		return listByStreamHash(user, hash, page, 20);
	}
	
	/**
	 * Get a page of push subscriptions for the given stream hash, where
	 * each page contains up to per_page items. Results will be returned in
	 * ascending order by creation date.
	 * 
	 * @param User user     The user.
	 * @param int  page     The page number to fetch.
	 * @param int  per_page The number of items per page.
	 * @return ArrayList<PushSubscription>
	 * @throws EInvalidData
	 * @throws EAPIError
	 * @throws EAccessDenied
	 */
	static public ArrayList<PushSubscription> listByStreamHash(User user, String hash, int page, int per_page) throws EInvalidData, EAPIError, EAccessDenied {
		return listByStreamHash(user, hash, page, per_page, ORDERBY_CREATED_AT, ORDERDIR_ASC, false);
	}
	
	/**
	 * Get a page of push subscriptions for the given stream hash, where
	 * each page contains up to per_page items. Results will be ordered
	 * according to the supplied ordering parameters.
	 * 
	 * @param User user                The user.
	 * @param int  page                The page number to fetch.
	 * @param int  per_page            The number of items per page.
	 * @param String order_by          The field on which to order the results.
	 * @param String order_dir         The direction of the ordering.
	 * @param boolean include_finished True to include subscriptions against
	 *                                 finished historic queries.
	 * @return ArrayList<PushSubscription>
	 * @throws EInvalidData
	 * @throws EAPIError
	 * @throws EAccessDenied
	 */
	static public ArrayList<PushSubscription> listByStreamHash(User user, String hash, int page, int per_page, String order_by, String order_dir, boolean include_finished) throws EInvalidData, EAPIError, EAccessDenied {
		return list(user, "hash", hash, page, per_page, order_by, order_dir, include_finished);
	}
	
	/**
	 * Get a list of push subscriptions for the given stream hash. Limited
	 * to 100 results.Results will be returned in ascending order by creation
	 * date.
	 * 
	 * @param User user
	 * @return ArrayList<PushSubscription>
	 * @throws EInvalidData
	 * @throws EAPIError
	 * @throws EAccessDenied
	 */
	static public ArrayList<PushSubscription> listByPlaybackId(User user, String playback_id) throws EInvalidData, EAPIError, EAccessDenied {
		return listByPlaybackId(user, playback_id, 1, 100);
	}
	
	/**
	 * Get a page of push subscriptions for the given stream hash, where
	 * each page contains up to 20 items. Results will be returned in
	 * ascending order by creation date.
	 * 
	 * @param User user The user.
	 * @param int  page The page number to fetch.
	 * @return ArrayList<PushSubscription>
	 * @throws EInvalidData
	 * @throws EAPIError
	 * @throws EAccessDenied
	 */
	static public ArrayList<PushSubscription> listByPlaybackId(User user, String playback_id, int page) throws EInvalidData, EAPIError, EAccessDenied {
		return listByPlaybackId(user, playback_id, page, 20);
	}
	
	/**
	 * Get a page of push subscriptions for the given stream hash, where
	 * each page contains up to per_page items. Results will be returned in
	 * ascending order by creation date.
	 * 
	 * @param User user     The user.
	 * @param int  page     The page number to fetch.
	 * @param int  per_page The number of items per page.
	 * @return ArrayList<PushSubscription>
	 * @throws EInvalidData
	 * @throws EAPIError
	 * @throws EAccessDenied
	 */
	static public ArrayList<PushSubscription> listByPlaybackId(User user, String playback_id, int page, int per_page) throws EInvalidData, EAPIError, EAccessDenied {
		return listByPlaybackId(user, playback_id, page, per_page, ORDERBY_CREATED_AT, ORDERDIR_ASC, false);
	}
	
	/**
	 * Get a page of push subscriptions for the given stream hash, where
	 * each page contains up to per_page items. Results will be ordered
	 * according to the supplied ordering parameters.
	 * 
	 * @param User user                The user.
	 * @param int  page                The page number to fetch.
	 * @param int  per_page            The number of items per page.
	 * @param String order_by          The field on which to order the results.
	 * @param String order_dir         The direction of the ordering.
	 * @param boolean include_finished True to include subscriptions against
	 *                                 finished historic queries.
	 * @return ArrayList<PushSubscription>
	 * @throws EInvalidData
	 * @throws EAPIError
	 * @throws EAccessDenied
	 */
	static public ArrayList<PushSubscription> listByPlaybackId(User user, String playback_id, int page, int per_page, String order_by, String order_dir, boolean include_finished) throws EInvalidData, EAPIError, EAccessDenied {
		return list(user, "playback_id", playback_id, page, per_page, order_by, order_dir, include_finished);
	}
	
	/**
	 * Get a page of push subscriptions in the given user's account, where
	 * each page contains up to per_page items. Results will be ordered
	 * according to the supplied ordering parameters.
	 * 
	 * @param User user                The user.
	 * @param int  page                The page number to fetch.
	 * @param int  per_page            The number of items per page.
	 * @param String order_by          The field on which to order the results.
	 * @param String order_dir         The direction of the ordering.
	 * @param boolean include_finished True to include subscriptions against
	 *                                 finished historic queries.
	 * @return ArrayList<PushSubscription>
	 * @throws EInvalidData
	 * @throws EAPIError
	 * @throws EAccessDenied
	 */
	static protected ArrayList<PushSubscription> list(User user, String hash_type, String hash, int page, int per_page, String order_by, String order_dir, boolean include_finished) throws EInvalidData, EAPIError, EAccessDenied {
		HashMap<String, String> params = new HashMap<String, String>();

		if (hash_type != null && hash_type.length() > 0) {
			if (!hash_type.equals("hash") && !hash_type.equals("playback_id")) {
				throw new EInvalidData("Hash type is invalid");
			}
			params.put(hash_type, hash);
		}
		
		if (page < 1) {
			throw new EInvalidData("The specified page number is invalid");
		}
		
		if (per_page < 1) {
			throw new EInvalidData("The specified per_page value is invalid");
		}
		
		if (order_by != ORDERBY_ID && order_by != ORDERBY_CREATED_AT) {
			throw new EInvalidData("The specified order_by is not supported");
		}

		if (order_dir != ORDERDIR_ASC && order_dir != ORDERDIR_DESC) {
			throw new EInvalidData("The specified order_dir is not supported");
		}
		
		params.put("page", String.valueOf(page));
		params.put("per_page", String.valueOf(per_page));
		params.put("order_by", order_by);
		params.put("order_dir", order_dir);
		
		if (include_finished) {
			params.put("include_finished", "1");
		}

		JSONObject res = user.callAPI("push/get", params);

		ArrayList<PushSubscription> retval = new ArrayList<PushSubscription>();

		try {
	        JSONArray subscriptions = res.getJSONArray("subscriptions");
	        for (int i = 0; i < subscriptions.length(); i++) {
	            retval.add(new PushSubscription(user, subscriptions.getJSONObject(i)));
	        }
		} catch (JSONException e) {
			throw new EAPIError("Failed to read the subscriptions from the response");
		}
		
		return retval;
	}
	
	/**
     * Get the most recent push subscription log entries.
     * 
     * @param User user The user making the request.
     * @return ArrayList<LogEntry>
	 * @throws EInvalidData 
	 * @throws EAPIError 
	 * @throws EAccessDenied 
     */
	public static Log getLogs(User user) throws EAPIError, EInvalidData, EAccessDenied {
		return getLogs(user, null);
	}
    
	/**
     * Get the most recent push subscription log entries.
     * 
     * @param User   user The user making the request.
     * @param String id   Optional subscription ID, or null.
     * @return ArrayList<LogEntry>
	 * @throws EInvalidData 
	 * @throws EAPIError 
	 * @throws EAccessDenied 
     */
	public static Log getLogs(User user, String id) throws EAPIError, EInvalidData, EAccessDenied {
		return getLogs(user, id, 1, 20);
	}
    
    /**
     * Page through recent push subscription log entries.
     * 
     * @param User user     The user making the request.
     * @param int  page     Which page to fetch.
     * @param int  per_page Based on this page size.
     * @return ArrayList<LogEntry>
     * @throws EInvalidData 
     * @throws EAPIError 
     * @throws EAccessDenied 
     */
    public static Log getLogs(User user, int page, int per_page) throws EAPIError, EInvalidData, EAccessDenied {
		return getLogs(user, null, page, per_page, ORDERBY_REQUEST_TIME, ORDERDIR_DESC);
    }

    /**
     * Page through recent push subscription log entries.
     * 
     * @param User   user   The user making the request.
     * @param String id     Optional subscription ID, or null.
     * @param int  page     Which page to fetch.
     * @param int  per_page Based on this page size.
     * @return ArrayList<LogEntry>
     * @throws EInvalidData 
     * @throws EAPIError 
     * @throws EAccessDenied 
     */
    public static Log getLogs(User user, String id, int page, int per_page) throws EAPIError, EInvalidData, EAccessDenied {
		return getLogs(user, id, page, per_page, ORDERBY_REQUEST_TIME, ORDERDIR_DESC);
    }

    /**
     * Page through recent push subscription log entries, specifying the sort
     * order.
     * 
     * @param User   user      The user making the request.
     * @param int    page      Which page to fetch.
     * @param int    per_page  Based on this page size.
     * @param String order_by  Which field to sort by.
     * @param String order_dir In asc[ending] or desc[ending] order.
     * @return ArrayList<LogEntry>
     * @throws EAPIError 
     * @throws EInvalidData 
     * @throws EAccessDenied 
     */
    public static Log getLogs(User user, int page, int per_page, String order_by, String order_dir) throws EAPIError, EInvalidData, EAccessDenied {
    	return getLogs(user, null, page, per_page, order_by, order_dir);
    }

    /**
     * Page through recent push subscription log entries, specifying the sort
     * order.
     * 
     * @param User   user      The user making the request.
     * @param int    page      Which page to fetch.
     * @param int    per_page  Based on this page size.
     * @param String order_by  Which field to sort by.
     * @param String order_dir In asc[ending] or desc[ending] order.
     * @return ArrayList<LogEntry>
     * @throws EAPIError 
     * @throws EInvalidData 
     * @throws EAccessDenied 
     */
    public static Log getLogs(User user, String id, int page, int per_page, String order_by, String order_dir) throws EAPIError, EInvalidData, EAccessDenied {
		HashMap<String, String> params = new HashMap<String, String>();

		if (page < 1) {
			throw new EInvalidData("The specified page number is invalid");
		}
		
		if (per_page < 1) {
			throw new EInvalidData("The specified per_page value is invalid");
		}
		
		if (order_by != ORDERBY_REQUEST_TIME) {
			throw new EInvalidData("The specified order_by is not supported");
		}

		if (order_dir != ORDERDIR_ASC && order_dir != ORDERDIR_DESC) {
			throw new EInvalidData("The specified order_dir is not supported");
		}
		
		if (id != null && id.length() > 0) {
			params.put("id", id);
		}
		params.put("page", String.valueOf(page));
		params.put("per_page", String.valueOf(per_page));
		params.put("order_by", order_by);
		params.put("order_dir", order_dir);

		JSONObject res = user.callAPI("push/log", params);

		Log retval = null;

		try {
			retval = new Log(res);
		} catch (JSONException e) {
			throw new EAPIError("Failed to read the logs from the response: " + e.getMessage());
		}
		
		return retval;
    }

    /**
     * @var String The subscription ID.
     */
	protected String _id = "";
	
	/**
	 * @var Date The date this subscription was created.
	 */
	protected Date _created_at = null;
	
	/**
	 * @var String The name of this subscription.
	 */
	protected String _name = "";
	
	/**
	 * @var String The current status of this subscription.
	 */
	protected String _status = "";
	
	/**
	 * @var String The hash to which this subscription is subscribed.
	 */
	protected String _hash = "";
	
	/**
	 * @var String "stream" or "historic"
	 */
	protected String _hash_type = "";
	
	/**
	 * @var Date The date/time of the last push request.
	 */
	protected Date _last_request = null;
	
	/**
	 * @var Date The date/time of the last successful push request.
	 */
	protected Date _last_success = null;
	
	/**
	 * @var boolean True if this subscription has been deleted (becomes
	 *              read-only).
	 */
	protected boolean _deleted = false;
	
	/**
	 * Construct a PushSubscription object from a JSONObject.
	 * 
	 * @param User       user The user that owns this subscription.
	 * @param JSONObject json The JSON object containing the subscription details.
	 * @throws EInvalidData
	 */
	public PushSubscription(User user, JSONObject json) throws EInvalidData {
		super(user);
		init(json);
	}
	
	/**
	 * Extract data from a JSONObject.
	 * 
	 * @param JSONObject json The JSONObject containing the data.
	 * @throws EInvalidData
	 */
	protected void init(JSONObject json) throws EInvalidData {
		try {
			_id = json.getString("id");
		} catch (JSONException e) {
			throw new EInvalidData("No id found");
		}
		
		try {
			_name = json.getString("name");
		} catch (JSONException e) {
			throw new EInvalidData("No name found");
		}
		
		try {
			_created_at = new Date(json.getLong("created_at") * 1000);
		} catch (JSONException e) {
			throw new EInvalidData("No created_at found");
		}
		
		try {
			_status = json.getString("status");
		} catch (JSONException e) {
			throw new EInvalidData("No status found");
		}
		
		try {
			_hash_type = json.getString("hash_type");
		} catch (JSONException e) {
			throw new EInvalidData("No hash_type found");
		}
		
		try {
			_hash = json.getString("hash");
		} catch (JSONException e) {
			throw new EInvalidData("No hash found");
		}
		
		try {
			_last_request = new Date(json.getLong("last_request") * 1000);
		} catch (JSONException e) {
			_last_request = null;
		}
		
		try {
			_last_success = new Date(json.getLong("last_success") * 1000);
		} catch (JSONException e) {
			_last_success = null;
		}
		
		try {
			_output_type = json.getString("output_type");
		} catch (JSONException e) {
			throw new EInvalidData("No output_type found");
		}
		
		try {
			_output_params.clear();
			_output_params.parse(json.getJSONObject("output_params"));
		} catch (JSONException e) {
			throw new EInvalidData("No valid output_params found");
		}
	}
	
	/**
	 * Re-fetch this subscription from the DataSift API.
	 *  
	 * @throws EInvalidData
	 * @throws EAPIError
	 * @throws EAccessDenied
	 */
	public void reload() throws EInvalidData, EAPIError, EAccessDenied {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("id", getId());
		init(_user.callAPI("push/get", params));
	}
	
	/**
	 * Get the subscription ID.
	 * 
	 * @return String
	 */
	public String getId() {
		return _id;
	}
	
	/**
	 * Get the subscription name.
	 * 
	 * @return String
	 */
	public String getName() {
		return _name;
	}
	
	/**
	 * Set an output parameter. Checks to see if the subscription has been
	 * deleted, and if not calls the base class to set the parameter.
	 * 
	 * @param String key The output parameter to set.
	 * @param String val The value to which to set it.
	 * @throws EInvalidData
	 */
	public void setOutputParam(String key, String val) throws EInvalidData {
		if (isDeleted()) {
			throw new EInvalidData("Cannot modify a deleted subscription");
		}
		super.setOutputParam(key, val);
	}

	/**
	 * Get the date this subscription was created.
	 * 
	 * @return Date
	 */
	public Date getCreatedAt() {
		return _created_at;
	}

	/**
	 * Get the current status of this subscription.
	 * 
	 * @return String
	 * @see PushSubscription.STATUS_*
	 */
	public String getStatus() {
		return _status;
	}
	
	/**
	 * Returns true if this subscription has been deleted.
	 * 
	 * @return boolean
	 */
	public boolean isDeleted() {
		return getStatus() == STATUS_DELETED;
	}
	
	/**
	 * Get the hash type to which this subscription is subscribed.
	 * 
	 * @return String
	 */
	public String getHashType() {
		return _hash_type;
	}
	
	/**
	 * Get the hash or playback ID to which this subscription is subscribed.
	 * 
	 * @return String
	 */
	public String getHash() {
		return _hash;
	}
	
	/**
	 * Get the output type.
	 * 
	 * @return String
	 */
	public String getOutputType() {
		return _output_type;
	}
	
	/**
	 * Get the date/time of the last push request.
	 * 
	 * @return Date
	 */
	public Date getLastRequest() {
		return _last_request;
	}
	
	/**
	 * Get the date/time of the last successful push request.
	 * 
	 * @return Date
	 */
	public Date getLastSuccess() {
		return _last_success;
	}
	
	/**
	 * Save changes to the name and output_parameters of this subscription.
	 * 
	 * @throws EInvalidData
	 * @throws EAPIError
	 * @throws EAccessDenied
	 */
	public void save() throws EInvalidData, EAPIError, EAccessDenied {
		// Can only update the name and output_params
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("id", getId());
		for (String key : _output_params.keySet()) {
			params.put("output_params." + key, _output_params.get(key));
		}
		params.put("name", getName());

		// Call the API and pass the returned object into init to update this object
		init(_user.callAPI("push/update", params));
	}
	
	/**
	 * Pause this subscription.
	 * 
	 * @throws EInvalidData
	 * @throws EAPIError
	 * @throws EAccessDenied
	 */
	public void pause() throws EInvalidData, EAPIError, EAccessDenied {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("id", String.valueOf(getId()));
		init(_user.callAPI("push/pause", params));
	}
	
	/**
	 * Resume this subscription.
	 * 
	 * @throws EInvalidData
	 * @throws EAPIError
	 * @throws EAccessDenied
	 */
	public void resume() throws EInvalidData, EAPIError, EAccessDenied {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("id", String.valueOf(getId()));
		init(_user.callAPI("push/resume", params));
	}
	
	/**
	 * Stop this subscription.
	 * 
	 * @throws EInvalidData
	 * @throws EAPIError
	 * @throws EAccessDenied
	 */
	public void stop() throws EInvalidData, EAPIError, EAccessDenied {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("id", String.valueOf(getId()));
		init(_user.callAPI("push/stop", params));
	}
	
	/**
	 * Delete this subscription.
	 * 
	 * @throws EAPIError
	 * @throws EAccessDenied
	 */
	public void delete() throws EAPIError, EAccessDenied {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("id", String.valueOf(getId()));
		_user.callAPI("push/delete", params);
		// The delete API call doesn't return the object, so set the status
		// manually
		_status = STATUS_DELETED;
	}
	
	/**
	 * Get the log for this subscription.
	 * 
	 * @return Log
	 * @throws EAPIError
	 * @throws EInvalidData
	 * @throws EAccessDenied
	 */
	public Log getLog() throws EAPIError, EInvalidData, EAccessDenied {
		return getLogs(_user, getId());
	}
	
	/**
	 * Get a page of the log for this subscription.
	 * 
	 * @param int page     The page to get.
	 * @param int per_page The number of entries per page.
	 * @return Log
	 * @throws EAPIError
	 * @throws EInvalidData
	 * @throws EAccessDenied
	 */
	public Log getLog(int page, int per_page) throws EAPIError, EInvalidData, EAccessDenied {
		return getLogs(_user, getId(), page, per_page);
	}
	
	/**
	 * Get a page of the log for this subscription order as specified.
	 * 
	 * @param int    page      The page to get.
	 * @param int    per_page  The number of entries per page.
	 * @param String order_by  By which field to order the entries. 
	 * @param String order_dir The direction of the sorting ("asc" or "desc").
	 * @return Log
	 * @throws EAPIError
	 * @throws EInvalidData
	 * @throws EAccessDenied
	 */
	public Log getLog(int page, int per_page, String order_by, String order_dir) throws EAPIError, EInvalidData, EAccessDenied {
		return getLogs(_user, getId(), page, per_page, order_by, order_dir);
	}
}
