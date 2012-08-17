/**
 * This example stops one or more push subscriptions in your account.
 */
package org.datasift.examples.push;

import org.datasift.EAPIError;
import org.datasift.EAccessDenied;
import org.datasift.EInvalidData;
import org.datasift.PushSubscription;
import org.datasift.examples.push.Env;

public class Stop {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Set up the environment
		Env.init(args);
		
		// Make sure we have at least one subscription ID
		if (Env.getArgCount() == 0) {
			System.err.println("Please specify one or more subscriptions to stop!");
			System.exit(1);
		}
		
		// Delete the subscriptions given on the command line
		for (int i = 0; i < Env.getArgCount(); i++) {
			try {
				String subscription_id = Env.getArg(i);
				
				PushSubscription sub = Env.getUser().getPushSubscription(subscription_id);
				System.out.print("Stopping " + subscription_id + ", " + sub.getName() + "...");
				sub.pause();
				System.out.println("done");
			} catch (EInvalidData e) {
				System.err.println("InvalidData: " + e.getMessage());
			} catch (EAPIError e) {
				System.err.println("APIError: " + e.getMessage());
			} catch (EAccessDenied e) {
				System.err.println("AccessDenied: " + e.getMessage());
			}
		}
		
		System.out.println("Rate-limit-remaining: " + Env.getUser().getRateLimitRemaining());
	}

}
