/**
 * This example resumes one or more push subscriptions in your account.
 */
package org.datasift.examples.push;

import org.datasift.EAPIError;
import org.datasift.EAccessDenied;
import org.datasift.EInvalidData;
import org.datasift.PushSubscription;

public class Resume {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Set up the environment
		Env.init(args);
		
		// Make sure we have at least one subscription ID
		if (Env.getArgCount() == 0) {
			System.err.println("Please specify one or more subscriptions to resume!");
			System.exit(1);
		}
		
		// Delete the subscriptions given on the command line
		for (int i = 0; i < Env.getArgCount(); i++) {
			try {
				String subscription_id = Env.getArg(i);
				
				PushSubscription sub = Env.getUser().getPushSubscription(subscription_id);
				System.out.print("Resuming " + subscription_id + ", " + sub.getName() + "...");
				sub.resume();
				System.out.println("done");
			} catch (EInvalidData e) {
				System.err.println("InvalidData: " + e.getMessage());
			} catch (EAPIError e) {
				System.err.println("APIError: " + e.getMessage());
			} catch (EAccessDenied e) {
				System.err.println("AccessDenied: " + e.getMessage());
			}
		}
	}

}
