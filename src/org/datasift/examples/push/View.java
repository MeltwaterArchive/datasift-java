/**
 * This example deletes a push subscription from your account.
 */
package org.datasift.examples.push;

import org.datasift.EAPIError;
import org.datasift.EAccessDenied;
import org.datasift.EInvalidData;

public class View {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Set up the environment
		Env.init(args);
		
		// Make sure we have at least one subscription ID
		if (Env.getArgCount() == 0) {
			System.err.println("Please specify one or more subscriptions to view!");
			System.exit(1);
		}
		
		// View the subscriptions given on the command line
		for (int i = 0; i < Env.getArgCount(); i++) {
			try {
				String subscription_id = Env.getArg(i);
				
				Env.displaySubscriptionDetails(Env.getUser().getPushSubscription(subscription_id));
				System.out.println("--");
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
