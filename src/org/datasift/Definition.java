/**
 * This file contains the Definition class.
 */
package org.datasift;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * The DataSift_Definition class represents a stream definition.
 * 
 * @author MediaSift
 * @version 0.1
 */
public class Definition {
	/**
	 * The user object that owns this definition.
	 * 
	 * @access protected
	 */
	protected User _user = null;

	/**
	 * The CSDL for this definition.
	 * 
	 * @access protected
	 */
	protected String _csdl = "";

	/**
	 * @access protected
	 */
	protected String _hash = "";

	/**
	 * @access protected
	 */
	protected Date _created_at = null;

	/**
	 * @access protected
	 */
	protected int _total_cost = -1;

	/**
	 * Constructor. A User object is required.
	 * 
	 * @access public
	 * @param User
	 *            user The user object.
	 */
	public Definition(User user) {
		this(user, "", "");
	}

	/**
	 * Constructor with a default CSDL definition.
	 * 
	 * @access public
	 * @param User
	 *            user The user object.
	 * @param String
	 *            csdl The initial CSDL.
	 */
	public Definition(User user, String csdl) {
		this(user, csdl, "");
	}

	/**
	 * Constructor with a default CSDL definition and hash.
	 * 
	 * @access public
	 * @param User
	 *            user The user object.
	 * @param string
	 *            csdl An optional default CSDL definition string.
	 */
	public Definition(User user, String csdl, String hash) {
		_user = user;
		set(csdl);
		_hash = hash;
	}

	/**
	 * Returns the definition CSDL string.
	 * 
	 * @access public
	 * @return String The definition CSDL.
	 */
	public String get() {
		return _csdl;
	}

	/**
	 * Sets the definition CSDL string.
	 * 
	 * @access public
	 * @param String
	 *            csdl The new definition CSDL string.
	 */
	public void set(String csdl) {
		// If the string has changed, reset the hash
		if (_csdl != csdl) {
			clearHash();
		}

		_csdl = csdl.trim();
	}

	/**
	 * Returns the hash for this definition. If the hash has not yet been
	 * obtained it compiles the definition first.
	 * 
	 * @access public
	 * @return String The hash.
	 * @throws EInvalidData
	 * @throws JSONException
	 * @throws EAccessDenied
	 */
	public String getHash() throws EInvalidData, EAccessDenied {
		if (_hash.length() == 0) {
			// Catch any compilation errors so they don't pass up to the caller
			try {
				compile();
			} catch (ECompileFailed e) {
			}
		}
		return _hash;
	}

	/**
	 * Reset the hash to false. The effect of this is to make the definition as
	 * requiring compilation.
	 * 
	 * @access protected
	 */
	protected void clearHash() {
		_hash = "";
		_created_at = null;
		_total_cost = -1;
	}

	/**
	 * Returns the created_at date which indicates when the stream was first
	 * created.
	 * 
	 * @return Date The date object.
	 * @throws EInvalidData
	 * @throws EAccessDenied
	 */
	public Date getCreatedAt() throws EInvalidData, EAccessDenied {
		if (_created_at == null) {
			// Catch any compilation errors so they don't pass up to the caller
			try {
				validate();
			} catch (ECompileFailed e) {
			}
		}
		return _created_at;
	}

	/**
	 * Returns the created_at date which indicates when the stream was first
	 * created.
	 * 
	 * @return Date The date object.
	 * @throws EInvalidData
	 * @throws EAccessDenied
	 */
	public int getTotalCost() throws EInvalidData, EAccessDenied {
		if (_total_cost == -1) {
			// Catch any compilation errors so they don't pass up to the caller
			try {
				validate();
			} catch (ECompileFailed e) {
			}
		}
		return _total_cost;
	}

	/**
	 * Call the DataSift API to validate this definition.
	 * 
	 * @access public
	 * @throws EInvalidData
	 * @throws ECompileFailed
	 * @throws JSONException
	 * @throws EAccessDenied
	 */
	public void validate() throws EInvalidData, ECompileFailed, EAccessDenied {
		if (_csdl.length() == 0) {
			throw new EInvalidData("Cannot validate an empty definition.");
		}

		JSONObject res = null;

		try {
			Hashtable<String, String> params = new Hashtable<String, String>();
			params.put("csdl", _csdl);

			res = _user.callAPI("validate", params);

			try {
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd H:m:s");
				_created_at = df.parse((String) res.get("created_at"));
			} catch (JSONException e) {
				throw new ECompileFailed(
					"Compiled successfully but no hash in the response"
				);
			} catch (ParseException e) {
				throw new EAPIError(
					"Compiled successfully but failed to parse the created_at date"
				);
			}

			try {
				_total_cost = Integer.parseInt((String) res.get("cost"));
			} catch (JSONException e) {
				throw new ECompileFailed(
					"Compiled successfully but no hash in the response"
				);
			}
		} catch (EAPIError e) {
			// Reset the hash
			clearHash();

			switch (e.getCode()) {
			case 400:
				// Compilation failed, we should have an error message
				try {
					throw new ECompileFailed(
						res != null ? (String) res.get("error")
							: "Validation failed but no error was returned"
					);
				} catch (JSONException ejson) {
					throw new ECompileFailed("No error message was provided");
				}

			default:
				throw new ECompileFailed("Unexpected APIError code: "
					+ e.getCode() + " [" + e.getMessage() + "]");
			}
		}
	}

	/**
	 * Call the DataSift API to compile this definition. On success it will
	 * store the returned hash.
	 * 
	 * @throws EInvalidData
	 * @throws ECompileFailed
	 * @throws JSONException
	 * @throws EAccessDenied
	 */
	public void compile() throws EInvalidData, ECompileFailed, EAccessDenied {
		if (_csdl.length() == 0) {
			throw new EInvalidData("Cannot compile an empty definition.");
		}

		JSONObject res = null;

		try {
			Hashtable<String, String> params = new Hashtable<String, String>();
			params.put("csdl", _csdl);

			res = _user.callAPI("compile", params);

			try {
				_hash = (String) res.get("hash");
			} catch (JSONException e) {
				throw new ECompileFailed(
					"Compiled successfully but no hash in the response");
			}

			try {
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd H:m:s");
				_created_at = df.parse((String) res.get("created_at"));
			} catch (JSONException e) {
				throw new ECompileFailed(
					"Compiled successfully but no hash in the response"
				);
			} catch (ParseException e) {
				throw new EAPIError(
					"Compiled successfully but failed to parse the created_at date"
				);
			}

			try {
				_total_cost = Integer.parseInt((String) res.get("cost"));
			} catch (JSONException e) {
				throw new ECompileFailed(
					"Compiled successfully but no hash in the response"
				);
			}
		} catch (EAPIError e) {
			// Reset the hash
			clearHash();

			switch (e.getCode()) {
			case 400:
				// Compilation failed, we should have an error message
				try {
					throw new ECompileFailed(
						res != null ? (String) res.get("error")
							: "Compilation failed but no error was returned"
					);
				} catch (JSONException ejson) {
					throw new ECompileFailed("No error message was provided");
				}

			default:
				throw new ECompileFailed("Unexpected APIError code: "
					+ e.getCode() + " [" + e.getMessage() + "]"
				);
			}
		}
	}

	/**
	 * 
	 * @return Cost
	 * @throws EInvalidData 
	 * @throws EAccessDenied 
	 * @throws ECompileFailed 
	 * @throws EAPIError 
	 */
	public Cost getCostBreakdown() throws EInvalidData, EAccessDenied, ECompileFailed, EAPIError {
		if (_csdl.length() == 0) {
			throw new EInvalidData("Cannot compile an empty definition.");
		}

		Cost retval = null;

		JSONObject res = null;

		Hashtable<String, String> params = new Hashtable<String, String>();

		params.put("hash", getHash());

		res = _user.callAPI("cost", params);

		try {
			retval = new Cost(res.toString());
		} catch (JSONException ejson) {
			throw new ECompileFailed(ejson.getMessage());
		}

		return retval;
	}

	/**
	 * Returns a DataSift_StreamConsumer-derived object for this definition, for
	 * the given type.
	 * 
	 * @access public
	 * @see StreamConsumer
	 * @param String
	 *            type The consumer type for which to construct a consumer.
	 * @throws EInvalidData
	 * @return StreamConsumer The consumer object.
	 * @throws EAccessDenied
	 * @throws ECompileFailed
	 */
	public StreamConsumer getConsumer(String type,
		IStreamConsumerEvents eventHandler) throws EInvalidData,
		ECompileFailed, EAccessDenied 
	{
		return StreamConsumer.factory(this._user, type, this, eventHandler);
	}
}
