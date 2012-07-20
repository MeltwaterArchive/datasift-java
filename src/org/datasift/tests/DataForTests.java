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
	public static int    push_id								= 123;
	public static String push_name								= "Push subscription for unit tests";
	public static Date   push_created_at						= null;
	public static String push_status							= "active";
	public static String push_hash_type							= "stream";
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
			
			historic_start	= df.parse("2012-06-01 00:00:00");
			historic_end	= df.parse("2012-06-01 23:59:59");
			
			push_created_at	= df.parse("2012-07-20 00:00:00");
		} catch (ParseException e) {
			// Not much we can do here
		}
	}
}
