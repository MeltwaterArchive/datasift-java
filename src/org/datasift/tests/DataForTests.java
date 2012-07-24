/**
 * This file contains the example data used by the unit tests.
 */
package org.datasift.tests;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class contains the example data used by the unit tests.
 */
final public class DataForTests {
	// Definition
	public static String definition			= "interaction.content contains \"datasift\"";
	public static String definition_hash	= "947b690ec9dca525fb8724645e088d79";
	public static double definition_dpu		= 0.1;
	public static String invalid_definition	= "interactin.content contains \"datasift\"";
	
	// Historic
	public static String historic_playback_id	= "93558e17de15072fa126370c37c5bd8f";
	public static Date   historic_start			= null;
	public static Date   historic_end			= null;
	public static String historic_feeds			= "twitter";
	public static int    historic_sample		= 10;
	public static String historic_name			= "Historic for unit tests";
	
	// PushSubscription
	public static String push_id								= "b665761917bbcb7afd3102b4a645b41e";
	public static String push_name								= "Push subscription for unit tests";
	public static Date   push_created_at						= null;
	public static String push_status							= "active";
	public static String push_hash_type							= "stream";
	public static Date   push_last_request						= null;
	public static Date   push_last_success						= null;
	public static String push_output_type						= "http";
	public static int    push_output_params_delivery_frequency	= 60;
	public static int    push_output_params_max_size			= 10240;
	public static String push_output_params_url					= "http://www.example.com/push_endpoint";
	public static String push_output_params_auth_type			= "basic";
	public static String push_output_params_auth_username		= "myuser";
	public static String push_output_params_auth_password		= "mypass";
	
	/**
	 * Initialise the test data (create the dates).
	 */
	public static void init()
	{
		try {
			DateFormat df	= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			historic_start		= df.parse("2012-06-01 00:00:00");
			historic_end		= df.parse("2012-06-01 23:59:59");
			
			push_created_at		= df.parse("2012-07-20 00:00:00");
			push_last_request	= df.parse("2012-07-20 07:10:00");
			push_last_success	= df.parse("2012-07-20 07:00:00");
		} catch (ParseException e) {
			// Not much we can do here
		}
	}
	
	public static String getHttpSubscriptionJson(String name, int delivery_frequency, int max_size, String url, String auth_type, String auth_user, String auth_pass) {
		return	"{\"id\":" + String.valueOf(push_id)+ ",\"name\":\"" + (name.length() == 0 ? push_name : name) + "\"," +
				"\"created_at\":" + String.valueOf(push_created_at.getTime() / 1000) + "," +
				"\"status\":\""+ push_status + "\",\"hash\":\"" + definition_hash + "\"," +
				"\"hash_type\":\"" + push_hash_type + "\",\"output_type\":\"" + push_output_type + "\"," +
				"\"last_request\":" + String.valueOf(push_last_request.getTime() / 1000) + ",\"last_success\":" + String.valueOf(push_last_success.getTime() / 1000) + "," + 
				"\"output_params\":{\"delivery_frequency\":" + String.valueOf(delivery_frequency == -1 ? push_output_params_delivery_frequency : delivery_frequency) + "," +
				"\"max_size\":" + String.valueOf(max_size == -1 ? push_output_params_max_size : max_size) + "," +
				"\"url\":\"" + (url.length() == 0 ? push_output_params_url : url) + "\"," +
				"\"auth\":{\"type\":\""+ (auth_type.length() == 0 ? push_output_params_auth_type : auth_type) + "\"," +
				"\"username\":\""+ (auth_user.length() == 0 ? push_output_params_auth_username : auth_user) + "\"," +
				"\"password\":\""+ (auth_pass.length() == 0 ? push_output_params_auth_password : auth_pass) + "\"}}}";
	}
}
