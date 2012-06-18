/**
 * This file contains the Historic class.
 */
package org.datasift;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * The DataSift_Historic class represents a historic query.
 * 
 * @author MediaSift
 * @version 0.1
 */
public class Historic {

    /**
     * @access protected
     */
    protected User _user = null;
    
    /**
     * @access protected
     */
    protected String _playback_id = "";
    
    /**
     * @access protected
     */
    protected double _dpus = -1;

    /**
     * @access protected
     */
    protected String _hash = "";
    
    /**
     * @access protected
     */
    protected Date _start = null;
    
    /**
     * @access protected
     */
    protected Date _end = null;
    
    /**
     * @access protected
     */
    protected int _sample = 100;
    
    /**
     * @access protected
     */
    protected String _feeds = "";
    
    /**
     * @access protected
     */
    protected String _name = "";
    
    /**
     * @access protected
     */
    protected String _status = "created";
    
    /**
     * @access protected
     */
    protected int _progress = 0;

    /**
     * Generate a name based on the current date/time.
     * 
     * @return The generated name.
     */
    public static String generateName()
    {
    	return new SimpleDateFormat("'historic_'yyyy-MM-dd_HH-mm-ss").format(new Date());
    }

    /**
     * Constructor.
     * 
     * @access public
     * @param User       user   The user object that owns this historic query.
     * @param Definition def    The stream hash with which to run the query.
     * @param Date       start  The date/time from which to run the query.
     * @param Date       end    The date/time at which to end the query.
     * @param String     feeds  Comma seperated list of interaction types required.
     * @param int        sample The sample required (%).
     */
    public Historic(User user, Definition def, Date start, Date end, String feeds, int sample) throws EInvalidData, EAccessDenied
    {
    	this(user, def.getHash(), start, end, feeds, sample);
    }
    
    /**
     * Constructor.
     * 
     * @access public
     * @param User       user   The user object that owns this historic query.
     * @param Definition def    The stream hash with which to run the query.
     * @param Date       start  The date/time from which to run the query.
     * @param Date       end    The date/time at which to end the query.
     * @param String     feeds  Comma seperated list of interaction types required.
     * @param int        sample The sample required (%).
     * @param String     name   The name of this query.
     */
    public Historic(User user, Definition def, Date start, Date end, String feeds, int sample, String name) throws EInvalidData, EAccessDenied
    {
    	this(user, def.getHash(), start, end, feeds, sample, name);
    }
    
    /**
     * Constructor.
     * 
     * @access public
     * @param User   user   The user object that owns this historic query.
     * @param String hash   The stream hash with which to run the query.
     * @param Date   start  The date/time from which to run the query.
     * @param Date   end    The date/time at which to end the query.
     * @param String feeds  Comma seperated list of interaction types required.
     * @param int    sample The sample required (%).
     */
    public Historic(User user, String hash, Date start, Date end, String feeds, int sample)
    {
    	this(user, hash, start, end, feeds, sample, "");
    }
    
    /**
     * Constructor.
     * 
     * @access public
     * @param User   user   The user object that owns this historic query.
     * @param String hash   The stream hash with which to run the query.
     * @param Date   start  The date/time from which to run the query.
     * @param Date   end    The date/time at which to end the query.
     * @param String feeds  Comma seperated list of interaction types required.
     * @param int    sample The sample required (%).
     * @param String name   The name of this query.
     */
    public Historic(User user, String hash, Date start, Date end, String feeds, int sample, String name) {
        _user = user;
        _hash = hash;
        _start = start;
        _end = end;
        _feeds = feeds;
        _sample = sample;
        _name = (name.length() == 0 ? generateName() : name);
    }
    
    /**
     * Construct a Historic object from an existing playback ID, loading the details from the API.
     * 
     * @param user
     * @param playback_id
     * @throws EAccessDenied 
     * @throws EInvalidData 
     * @throws EAPIError 
     */
    public Historic(User user, String playback_id) throws EInvalidData, EAccessDenied, EAPIError {
    	_user = user;
    	_playback_id = playback_id;
    	
    	reloadData();
    }
    
    /**
     * Get the start date.
     * 
     * @return Date
     */
    public Date getStartDate()
    {
    	return _start;
    }
    
    /**
     * Get the end date.
     * 
     * @return Date
     */
    public Date getEndDate()
    {
    	return _end;
    }
    
    /**
     * Get the historic name.
     * 
     * @return String
     */
    public String getName()
    {
    	return _name;
    }
    
    /**
     * Get the progress. To refresh this from the server call reloadData().
     * 
     * @return int
     */
    public int getProgress()
    {
    	return _progress;
    }
    
    /**
     * Get the current status. To refresh this from the server call reloadData().
     * 
     * @return String
     */
    public String getStatus()
    {
    	return _status;
    }
    
    /**
     * Set the name of this historic query. If the query has already been prepared this will send the change to the API.
     * 
     * @param name
     * @throws EInvalidData
     * @throws EAPIError
     * @throws EAccessDenied
     */
    public void setName(String name) throws EInvalidData, EAPIError, EAccessDenied
    {
    	if (_playback_id.length() == 0) {
    		_name = name;
    	} else {
            try {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("id", _playback_id);
                params.put("name", name);

                _user.callAPI("historics/update", params);
                
                reloadData();
            } catch (EAPIError e) {
                switch (e.getCode()) {
                    case 400:
                        // Missing or invalid parameters
                        throw new EInvalidData(e.getMessage());

                    default:
                        throw new EAPIError("Unexpected APIError code: "
                                + e.getCode() + " [" + e.getMessage() + "]");
                }
            }
    	}
    }
    
    /**
     * Returns the playback ID for this historic. If the historic has not yet
     * been prepared that will be done automagically to obtain the ID.
     * 
     * @return String The playback ID.
     * @throws EAPIError 
     * @throws EAccessDenied 
     * @throws EInvalidData 
     */
    public String getHash() throws EInvalidData, EAccessDenied, EAPIError
    {
    	if (_playback_id.length() == 0) {
    		prepare();
    	}
    	return _playback_id;
    }
    
    /**
     * Returns the DPU cost of running this historic. If the historic has not
     * yet been prepared that will be done automagically to obtain the cost.
     * 
     * @return double The DPU cost.
     * @throws EAPIError 
     * @throws EAccessDenied 
     * @throws EInvalidData 
     */
    public double getDPUs() throws EInvalidData, EAccessDenied, EAPIError
    {
    	if (_playback_id.length() == 0) {
    		prepare();
    	}
    	return _dpus;
    }
    
    /**
     * Reload the data for this historic from the DataSift API.
     * 
     * @throws EInvalidData
     * @throws EAccessDenied
     * @throws EAPIError
     */
    public void reloadData() throws EInvalidData, EAccessDenied, EAPIError
    {
    	if (_playback_id.length() == 0) {
    		throw new EInvalidData("Cannot reload the data for a historic query that hasn't been prepared.");
    	}

        JSONObject res = null;

        try {
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("id", _playback_id);

            res = _user.callAPI("historics/get", params);
            
            try {
            	_name = res.getString("name");
			} catch (JSONException e) {
                throw new EAPIError(
                        "Historic retrieved successfully but no name in the response.");
            }

            try {
            	_start = new Date(res.getLong("start") * 1000);
			} catch (JSONException e) {
                throw new EAPIError(
                        "Historic retrieved successfully but no start timestmp in the response.");
            }

            try {
            	_end = new Date(res.getLong("end") * 1000);
			} catch (JSONException e) {
                throw new EAPIError(
                        "Historic retrieved successfully but no end timestmp in the response.");
            }
            
            try {
            	_status = res.getString("status");
			} catch (JSONException e) {
                throw new EAPIError(
                        "Historic retrieved successfully but no status in the response.");
            }
            
            try {
            	_progress = res.getInt("progress");
			} catch (JSONException e) {
                throw new EAPIError(
                        "Historic retrieved successfully but no progress in the response.");
            }

            try {
            	_end = new Date(res.getLong("created_at") * 1000);
			} catch (JSONException e) {
                throw new EAPIError(
                        "Historic retrieved successfully but no created at timestmp in the response.");
            }
            
//"feed": [ "digg" ], 
//"sample": 100, 
//"volume_info": { "digg" }
//            
        } catch (EAPIError e) {
            switch (e.getCode()) {
                case 400:
                    // Missing or invalid parameters
                    throw new EInvalidData(e.getMessage());

                default:
                    throw new EAPIError("Unexpected APIError code: "
                            + e.getCode() + " [" + e.getMessage() + "]");
            }
        }
	}
    
    /**
     * Call the DataSift API to prepare this historic query.
     * 
     * @throws EInvalidData
     * @throws EAccessDenied
     * @throws EAPIError
     */
    public void prepare() throws EInvalidData, EAccessDenied, EAPIError
    {
    	if (_playback_id.length() != 0) {
    		throw new EInvalidData("This historic query has already been prepared.");
    	}
    	
        JSONObject res = null;

        try {
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("hash", _hash);
            params.put("start", String.valueOf(_start.getTime() / 1000));
            params.put("end", String.valueOf(_end.getTime() / 1000));
            params.put("name", _name);
            params.put("feed", _feeds);

            res = _user.callAPI("historics/prepare", params);

            try {
				_playback_id = res.getString("id");
			} catch (JSONException e) {
                throw new EAPIError(
                        "Prepared successfully but no playback ID in the response.");
			}

            try {
                _dpus = res.getDouble("dpus");
            } catch (JSONException e) {
                throw new EAPIError(
                        "Prepared successfully but no DPU cost in the response.");
            }
        } catch (EAPIError e) {
            switch (e.getCode()) {
                case 400:
                    // Missing or invalid parameters
                    throw new EInvalidData(e.getMessage());

                default:
                    throw new EAPIError("Unexpected APIError code: "
                            + e.getCode() + " [" + e.getMessage() + "]");
            }
        }
    }
    
    /**
     * Start this historic query.
     * 
     * @throws EInvalidData
     * @throws EAccessDenied
     * @throws EAPIError
     */
    public void start() throws EInvalidData, EAccessDenied, EAPIError
    {
    	if (_playback_id.length() == 0) {
    		throw new EInvalidData("Cannot start a historic that hasn't been prepared.");
    	}
    	
        try {
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("id", _playback_id);
            _user.callAPI("historics/start", params);
        } catch (EAPIError e) {
            switch (e.getCode()) {
	            case 400:
	                // Missing or invalid parameters
	                throw new EInvalidData(e.getMessage());
	
	            case 404:
	                // Historic query not found
	                throw new EInvalidData(e.getMessage());

                default:
                    throw new EAPIError("Unexpected APIError code: "
                            + e.getCode() + " [" + e.getMessage() + "]");
            }
        }
    }

    /**
     * Returns a DataSift_StreamConsumer-derived object for this definition, for the given type.
	 * 
     * @param type
     * @param eventHandler
     * @return StreamConsumer
     * @throws EInvalidData
     * @throws ECompileFailed
     * @throws EAccessDenied
     * @throws EAPIError
     */
    public StreamConsumer getConsumer(String type, IStreamConsumerEvents eventHandler) throws EInvalidData, ECompileFailed, EAccessDenied, EAPIError
    {
        return StreamConsumer.historicFactory(this._user, type, this, eventHandler);
    }
}
