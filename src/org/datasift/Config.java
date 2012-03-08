/**
 * This file contains the configuration for the examples and unit tests.
 */
package org.datasift;

/**
 * This configuration is used by the examples and unit tests.
 */
final public class Config {
	public static String username = "<your_username>";
	public static String api_key = "<your_api_key>";

	public static String definition = "interaction.content contains \"datasift\"";
	public static String definition_hash = "947b690ec9dca525fb8724645e088d79";
	public static double definition_dpu = 0.1;

	public static String invalid_definition = "interactin.content contains \"datasift\"";
}
