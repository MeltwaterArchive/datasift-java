/**
 * This file contains the example data used by the unit tests.
 */
package org.datasift.tests;

/**
 * This class contains the example data used by the unit tests.
 */
final public class DataForTests {
	public static String definition = "interaction.content contains \"datasift\"";
	public static String definition_hash = "947b690ec9dca525fb8724645e088d79";
	public static int definition_dpu = 4;

	public static String invalid_definition = "interactin.content contains \"datasift\"";
	
	public static String recording = "{\"id\":\"47ce46821c942ff42f8e\",\"start_time\":1313055762,\"finish_time\":null,\"name\":\"Inherit everything 123\",\"hash\":\"9e2e0ba334ee76aa06ef42d5565dbb70\"}";
	public static String recording_id = "47ce46821c942ff42f8e";
	public static Integer recording_start_time = 1313055762;
	public static Integer recording_end_time = null;
	public static String recording_name = "Inherit everything 123";
	public static String recording_hash = "9e2e0ba334ee76aa06ef42d5565dbb70";
	
	public static String export = "{\"id\":\"82\",\"recording_id\":\"47ce46821c942ff42f8e\",\"name\":\"Some name\",\"start\":1313052000,\"end\":1312974000,\"status\":\"killed\"}";
	public static String export_id = "82";
	public static String export_recording_id = "47ce46821c942ff42f8e";
	public static String export_name = "Some name";
	public static int export_start = 1313052000;
	public static int export_end = 1312974000;
	public static String export_status = "killed";
}
