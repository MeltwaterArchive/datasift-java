/**
 * This file contains the example data used by the unit tests.
 */
package org.datasift.tests;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class contains the example data used by the unit tests.
 */
final public class DataForTests {
	public static String definition = "interaction.content contains \"datasift\"";
	public static String definition_hash = "947b690ec9dca525fb8724645e088d79";
	public static double definition_dpu = 0.1;

	public static String invalid_definition = "interactin.content contains \"datasift\"";
	
	public static String historic_playback_id = "93558e17de15072fa126370c37c5bd8f";
	public static Date historic_start = null;
	public static Date historic_end = null;
	public static String historic_feeds = "twitter";
	public static int historic_sample = 10;
	public static String historic_name = "Historic for unit tests";
	
	public static void init()
	{
		try {
			historic_start = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2012-06-01 00:00:00");
			historic_end = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2012-06-01 23:59:59");
		} catch (ParseException e) {
			// Not much we can do here
		}
	}
}
