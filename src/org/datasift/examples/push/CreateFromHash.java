/**
 * This example creates a push subscription in your account.
 */
package org.datasift.examples.push;

import org.datasift.EAPIError;
import org.datasift.EAccessDenied;
import org.datasift.EInvalidData;
import org.datasift.PushSubscription;
import org.datasift.pushsubscription.Http;

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
		
		// Check we have the right number for the output_type given
		String output_type = Env.getArg(0);
		if (output_type.toLowerCase().equals("http")) {
			int count = Env.getArgCount() - 4;
			if ((Env.getArg(5).equals(Http.AUTH_TYPE_NONE) && count != 2) || (!Env.getArg(5).equals(Http.AUTH_TYPE_NONE) && count != 4)) {
				usage();
			}
		} else {
			usage("Unknown output_type \"" + output_type + "\"!");
		}

		try {
			// Create a new PushSubscription
			PushSubscription sub = Env.getUser().createPushSubscription(output_type, Env.getArg(1), Env.getArg(2), Env.getArg(3));

			// Add the output_type-specific parameters
			if (output_type.toLowerCase().equals("http")) {
				((Http)sub).setUrl(Env.getArg(4));
				((Http)sub).setAuthType(Env.getArg(5));
				if (!Env.getArg(5).equals(Http.AUTH_TYPE_NONE)) {
					((Http)sub).setAuthUsername(Env.getArg(6));
					((Http)sub).setAuthPassword(Env.getArg(7));
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
		System.err.println("       url         = the url to which to post data");
		System.err.println("       auth_type   = none | basic");
		System.err.println("       auth_user   = the basic auth username (if auth_type == basic)");
		System.err.println("       auth_pass   = the basic auth password (if auth_type == basic)");
		System.err.println("");
		if (exit) {
			System.exit(1);
		}
	}

}
