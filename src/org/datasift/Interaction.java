/**
 * This file contains the Interaction class and provides access to an
 * interaction from a JSON source. It uses the JSONdn class to facilitate
 * dot-notation access, but also exposes the full JSONObject functionality
 * if required.
 */
package org.datasift;

import org.json.JSONException;

/**
 * @author MediaSift
 * @version 0.1
 */
public class Interaction extends JSONdn {

	public Interaction(String source) throws JSONException {
		super(source);
	}

}
