/**
 * This simple example demonstrates creating a historic query and directing
 * the output to an HTTP push endpoint.
 */
package org.datasift.examples.push;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.datasift.Definition;
import org.datasift.EAPIError;
import org.datasift.EAccessDenied;
import org.datasift.EInvalidData;
import org.datasift.PushSubscription;
import org.datasift.User;
import org.datasift.pushsubscription.HttpPushSubscription;
import org.datasift.pushsubscription.Log;
import org.datasift.pushsubscription.LogEntry;

/**
 * @author MediaSift
 * @version 0.1
 */
public class StreamToHttpPost {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.err
					.println("Usage: StreamDump <configuration_filename>");
			System.err.println("Where...");
			System.err
					.println("  <configuration_filename> the filename of a key/value file containing the configuration options");
			System.exit(1);
		}

		// Load the configuration
		Properties configFile = null;
		try {
			InputStream in = new FileInputStream(new File(args[0]));
			configFile = new Properties();
			configFile.load(in);
			in.close();
		} catch (FileNotFoundException e) {
			System.err.println("Configuration file not found!");
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Failed to read the configuration file: " + e.getMessage());
			System.exit(1);
		}
		
		// Set up the platform
		String platform = "";
		if (configFile.containsKey("platform") && !configFile.getProperty("platform").equals("datasift.com")) {
			platform = configFile.getProperty("platform");
			User._api_base_url = User._api_base_url.replace("datasift.com", platform);
			User._stream_base_url = User._stream_base_url.replace("datasift.com", platform);
			User._websocket_base_url = User._websocket_base_url.replace("datasift.com", platform);
		}
		
		HttpPushSubscription push = null;
		
		try {
			// Create the user, only using SSL on production
			System.out.println("Creating user...");
			User user = new User(configFile.getProperty("username"), configFile.getProperty("api_key"), platform.length() == 0);
			
			// Create the definition
			System.out.println("Creating definition...");
			Definition def = user.createDefinition(configFile.getProperty("csdl"));
			
			// Create the push subscription
			System.out.println("Creating push subscription...");
			push = (HttpPushSubscription) def.createPushSubscription("http", configFile.getProperty("push_name"));
			
			// Configure the HTTP-specific options
			push.setDeliveryFrequency(Integer.parseInt(configFile.getProperty("push_delivery_frequency")));
			if (configFile.containsKey("push_max_size")) {
				push.setMaxSize(Integer.parseInt(configFile.getProperty("push_max_size")));
			}
			push.setUrl(configFile.getProperty("push_url"));
			push.setAuthType(configFile.getProperty("push_auth_type"));
			if (configFile.containsKey("push_auth_username")) {
				push.setMaxSize(Integer.parseInt(configFile.getProperty("push_auth_username")));
			}
			if (configFile.containsKey("push_auth_password")) {
				push.setMaxSize(Integer.parseInt(configFile.getProperty("push_auth_password")));
			}
			
			// Work out for how long we should run, or default to 5 minutes
			long stopAfterSeconds = 0;
			try {
				stopAfterSeconds = Long.parseLong(configFile.getProperty("stop_after_seconds"));
			} catch (NumberFormatException e) {
				stopAfterSeconds = 300;
			}
			long endTime = (new Date().getTime()) + (stopAfterSeconds * 1000);
			
			// Save the subscription, which will send it to DataSift and activate it
			System.out.println("Activating push subscription and running for " + String.valueOf(stopAfterSeconds) + " seconds...");
			push.save();
			displayStatus(push);
			
			// Loop: wait 10 seconds, check the time, display the status
			while (push.getStatus().equals(PushSubscription.STATUS_ACTIVE) && !push.getStatus().equals(PushSubscription.STATUS_PAUSED)) {
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					// Ignored
				}
				
				// Fetch the latest info for our push subscription
				push.reload();
				
				// Display a summary of the current status
				displayStatus(push);

				// Have we come to the end?
				if (new Date().getTime() > endTime) {
					// Stop the subscription
					push.stop();
					System.out.println("Subscription stopped after " + String.valueOf(stopAfterSeconds) + " seconds");
					break;
				}
			}
		} catch (EInvalidData e) {
			System.err.println("EInvalidData: " + e.getMessage());
		} catch (EAccessDenied e) {
			System.err.println("EAccessDenied: " + e.getMessage());
		} catch (EAPIError e) {
			System.err.println("EAPIError: " + e.getMessage());
		} finally {
			// If we got as far as creating the PushSubscription object and activating it, stop it
			if (push != null && push.getId().length() > 0) {
				if (push.getStatus().equals(PushSubscription.STATUS_ACTIVE) || push.getStatus().equals(PushSubscription.STATUS_PAUSED)) {
					try {
						push.stop();
					} catch (EInvalidData e) {
						System.err.println("EInvalidData (while stopping): " + e.getMessage());
					} catch (EAccessDenied e) {
						System.err.println("EAccessDenied (while stopping): " + e.getMessage());
					} catch (EAPIError e) {
						System.err.println("EAPIError (while stopping): " + e.getMessage());
					}
				}

				System.out.println();
				String input = "";
				while (!input.equals("y") && !input.equals("n")) {
					input = System.console().readLine("Display log [Y/N]? ").toLowerCase();
				}
				
				if (input.equals("y")) {
					System.out.println("Log");
					System.out.println("===");
					System.out.println();
					try {
						Log log = push.getLog();
						if (log.getCount() == 0) {
							System.out.println("No log entries found.");
						} else {
							for (LogEntry logEntry : log) {
								System.out.println(
										new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(logEntry.getRequestTime()) + " [" +
										String.valueOf(logEntry.getSubscriptionId()) + "] " +
										(logEntry.getSuccess() ? "Success " : "") +
										logEntry.getMessage());
							}
						}
					} catch (EInvalidData e) {
						System.err.println("EInvalidData (while getting the log): " + e.getMessage());
					} catch (EAccessDenied e) {
						System.err.println("EAccessDenied (while getting the log): " + e.getMessage());
					} catch (EAPIError e) {
						System.err.println("EAPIError (while getting the log): " + e.getMessage());
					}
				}
				
				System.out.println();
			}
		}
	}
	
	private static void displayStatus(PushSubscription push) {
		if (push != null) {
			System.out.println("--");
			System.out.println("Current status: " + push.getStatus());
			System.out.println("  Last request: " + (push.getLastRequest() == null ? "none" : push.getLastRequest().toString()));
			System.out.println("  Last success: " + (push.getLastSuccess() == null ? "none" : push.getLastSuccess().toString()));
		}
	}
}
