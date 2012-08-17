/**
 * This example lists the current push subscriptions in your account.
 */
package org.datasift.examples.push;

import java.util.ArrayList;
import java.util.Iterator;

import org.datasift.EAPIError;
import org.datasift.EAccessDenied;
import org.datasift.EInvalidData;
import org.datasift.PushSubscription;
import org.datasift.examples.push.Env;

public class List {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			// Set up the environment
			Env.init(args);
			
			// Get subscriptions
			ArrayList<PushSubscription> subscriptions = Env.getUser().listPushSubscriptions();
			
			// Did we get any?
			if (subscriptions.size() == 0) {
				System.out.println("No subscriptions exist in your account.");
			} else {
				// Display the details
				Iterator<PushSubscription> i = subscriptions.iterator();
				while (i.hasNext()) {
				    Env.displaySubscriptionDetails(i.next());
				    System.out.println("--");
				}
			}
		} catch (EInvalidData e) {
			System.out.println("InvalidData: " + e.getMessage());
		} catch (EAPIError e) {
			System.out.println("APIError: " + e.getMessage());
		} catch (EAccessDenied e) {
			System.out.println("AccessDenied: " + e.getMessage());
		}
		
		System.out.println("Rate-limit-remaining: " + Env.getUser().getRateLimitRemaining());
	}

}
