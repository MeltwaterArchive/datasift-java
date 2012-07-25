/**
 * This example creates a push subscription in your account.
 */
package org.datasift.examples.push;

import org.datasift.EAPIError;
import org.datasift.EAccessDenied;
import org.datasift.EInvalidData;
import org.datasift.PushSubscription;
import org.datasift.pushsubscription.HttpPushSubscription;

public class CreateFromHash {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Set up the environment
		Env.init(args);
		
		// Check we have the right number of arguments
		if (Env.getArgCount() < 4) {
			usage();
		}

		// Common args
		String output_type = Env.getArg(0);
		String hash_type   = Env.getArg(1);
		String hash        = Env.getArg(2);
		String name        = Env.getArg(3);
		
		// Http output type args
		int    delivery_frequency = 10;
		String url                = "";
		String auth_type          = "";
		String auth_user          = "";
		String auth_pass          = "";

		// Check we have the right number for the output_type given
		
		if (output_type.toLowerCase().equals("http")) {
			int count = Env.getArgCount() - 4;
			
			auth_type = Env.getArg(6);
			
			if ((auth_type.equals("none") && count != 3) || (!auth_type.equals("none") && count != 5)) {
				usage();
			}
		} else {
			usage("Unknown output_type \"" + output_type + "\"!");
		}

		try {
			// Create a new PushSubscription
			PushSubscription sub = Env.getUser().createPushSubscription(output_type, hash_type, hash, name);

			// Add the output_type-specific parameters
			if (output_type.toLowerCase().equals("http")) {
				HttpPushSubscription http_sub = (HttpPushSubscription) sub;
				
				delivery_frequency = Integer.parseInt(Env.getArg(4));
				url = Env.getArg(5);

				http_sub.setDeliveryFrequency(delivery_frequency);
				http_sub.setUrl(url);
				http_sub.setAuthType(auth_type);
				
				if (!auth_type.equals("none")) {
					auth_user = Env.getArg(7);
					auth_pass = Env.getArg(8);
					
					http_sub.setAuthUsername(auth_user);
					http_sub.setAuthPassword(auth_pass);
				}
			} else {
				usage("Unhandled output_type \"" + output_type + "\"!");
			}

			// Save it
			sub.save();
			
			// Display the details of the new subscription
			Env.displaySubscriptionDetails(sub);
		} catch (EInvalidData e) {
			System.err.println("EInvalidData: " + e.getMessage());
		} catch (EAPIError e) {
			System.err.println("EAPIError: " + e.getMessage());
		} catch (EAccessDenied e) {
			System.err.println("EAccessDenied: " + e.getMessage());
		}
		
	}
	
	public static void usage() {
		usage("", true);
	}
	
	public static void usage(String message) {
		usage(message, true);
	}
	
	public static void usage(String message, boolean exit) {
		if (message.length() > 0) {
			System.err.println("");
			System.err.println(message);
		}
		System.err.println("");
		System.err.println("Usage: Create output_type hash_type hash name ...");
		System.err.println("");
		System.err.println("Where: output_type = http (currently only http is supported");
		System.err.println("       hash_type   = stream | historic");
		System.err.println("       hash        = the stream hash");
		System.err.println("       name        = a friendly name for the subscription");
		System.err.println("       ...         = output_type-specific arguments (see below)");
		System.err.println("");
		System.err.println("HTTP parameters");
		System.err.println("       delivery_frequency = how often data should be posted");
		System.err.println("       url                = the url to which to post data");
		System.err.println("       auth_type          = none | basic");
		System.err.println("       auth_user          = the basic auth username (if auth_type == basic)");
		System.err.println("       auth_pass          = the basic auth password (if auth_type == basic)");
		System.err.println("");
		if (exit) {
			System.exit(1);
		}
	}

}
