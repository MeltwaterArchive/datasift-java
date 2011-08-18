/**
 * This file contains the ApiClient class.
 */
package org.datasift;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * The ApiClient class provides access to the DataSift API.
 * 
 * @author MediaSift
 * @version 0.3
 */
public class ApiClient {

	private HttpClient _http_client = null;
	private User _user = null;

	public ApiClient(User user) {
		_http_client = new DefaultHttpClient();
		_user = user;
	}

	/**
	 * Make a call to the API.
	 * 
	 * @param user
	 *            The user object.
	 * @param endpoint
	 *            The endpoint to be called.
	 * @param params
	 *            The GET parameters to pass to the API.
	 * @return ApiResponse
	 * @throws EAPIError
	 */
	public ApiResponse call(String endpoint,
			HashMap<String, String> params) throws EAPIError {

		ApiResponse retval = null;

		try {
			List<NameValuePair> qparams = new ArrayList<NameValuePair>();
			for (String key : params.keySet()) {
				qparams.add(new BasicNameValuePair(key, params.get(key)));
			}

			HttpGet get = new HttpGet("http://" + User._api_base_url + endpoint
					+ ".json?" + URLEncodedUtils.format(qparams, "UTF-8"));
			get.addHeader("Auth", _user.getUsername() + ":" + _user.getAPIKey());

			HttpResponse response = _http_client.execute(get);

			HttpEntity entity = response.getEntity();

			// Update the rate limits from the headers
			int rate_limit = -1;
			int rate_limit_remaining = -1;
			Header[] headers = response.getAllHeaders();
			for (int i = 0; i < headers.length; i++) {
				String name = headers[i].getName();
				if (name.equalsIgnoreCase("x-ratelimit-limit")) {
					rate_limit = Integer.parseInt(headers[i].getValue());
				} else if (name.equalsIgnoreCase("x-ratelimit-remaining")) {
					rate_limit_remaining = Integer.parseInt(headers[i]
							.getValue());
				}
			}

			String body = "";
			if (entity != null) {
				body = EntityUtils.toString(entity);
//				System.out.println(endpoint);
//				System.out.println(response.getStatusLine().getStatusCode());
//				System.out.println(response.getStatusLine().getReasonPhrase());
//				System.out.println(body);
//				System.out.println("----------");
			}

			retval = new ApiResponse(response.getStatusLine().getStatusCode(),
					response.getStatusLine().getReasonPhrase(), body,
					rate_limit, rate_limit_remaining);

		} catch (ParseException e) {
			throw new EAPIError("Failed to parse response: " + e.getMessage());
		} catch (IOException e) {
			throw new EAPIError("Network error: " + e.getMessage());
		}
		
		return retval;
	}
}
