/**
 * This example creates a push subscription in your account.
 */
package org.datasift.examples.push;

import org.datasift.EAPIError;
import org.datasift.EAccessDenied;
import org.datasift.EInvalidData;
import org.datasift.PushDefinition;
import org.datasift.PushSubscription;

public class PushFromHash {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Set up the environment
		Env.init(args);
		
		// Check we have enough arguments
		if (Env.getArgCount() < 4) {
			usage();
		}

		// Fixed args
		String output_type = Env.getArg(0);
		String hash_type   = Env.getArg(1);
		String hash        = Env.getArg(2);
		String name        = Env.getArg(3);
		
		// The rest of the args will be output parameters, and we'll use them later
		
		try {
			// Create the PushDefinition
			PushDefinition pushDef = Env.getUser().createPushDefinition();
			pushDef.setOutputType(output_type);
			
			// Now add the output_type-specific args from the command line
			for (int i = 4; i < Env.getArgCount(); i++) {
				String[] bits = Env.getArg(i).split("=", 2);
				if (bits.length != 2) {
					usage("Invalid output_param: " + Env.getArg(i));
				}
				pushDef.setOutputParam(bits[0], bits[1]);
			}

			// Subscribe the definition to the hash
			PushSubscription pushSub = null;
			if (hash_type.equals("stream")) {
				pushSub = pushDef.subscribeStreamHash(hash, name);
			} else if (hash_type.equals("historic")) {
				pushSub = pushDef.subscribeHistoricPlaybackId(hash, name);
			} else {
				usage("Invalid hash_type: " + hash_type);
			}

			// Display the details of the new subscription
			Env.displaySubscriptionDetails(pushSub);
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
		System.err.println("Usage: PushFromHash \\");
		System.err.println("             <username> <api_key> <output_type> <hash_type> <hash> <name> ...");
		System.err.println("");
		System.err.println("Where: output_type = http (currently only http is supported)");
		System.err.println("       hash_type   = stream | historic");
		System.err.println("       hash        = the hash");
		System.err.println("       name        = a friendly name for the subscription");
		System.err.println("       key=val     = output_type-specific arguments");
		System.err.println("");
		System.err.println("Example");
		System.err.println("       PushFromHash http stream <hash> \"Push Name\" delivery_frequency=10 \\");
		System.err.println("                    url=http://www.example.com/push_endpoint auth.type=none");
		System.err.println("");
		if (exit) {
			System.exit(1);
		}
	}

}
