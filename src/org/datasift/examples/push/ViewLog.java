/**
 * This example displays the logs for all subscriptions in your account, or
 * just those related to a specific subscription.
 */
package org.datasift.examples.push;

import java.text.SimpleDateFormat;

import org.datasift.EAPIError;
import org.datasift.EAccessDenied;
import org.datasift.EInvalidData;
import org.datasift.PushSubscription;
import org.datasift.examples.push.Env;
import org.datasift.pushsubscription.Log;
import org.datasift.pushsubscription.LogEntry;

public class ViewLog {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Set up the environment
		Env.init(args);
		
		try {
			switch (Env.getArgCount()) {
				case 0:
					// Show the latest log entries
					Log log = Env.getUser().getPushSubscriptionLogs();
					
					if (log.getCount() == 0) {
						System.out.println("No log entries found.");
					} else {
						// Walk through the entries backwards, so the latest is shown last
						for (LogEntry l : log) {
							System.out.println(
									new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(l.getRequestTime()) + " [" +
									l.getSubscriptionId() + "] " +
									(l.getSuccess() ? "Success " : "") +
									l.getMessage());
						}
					}
					break;
					
				case 1:
					// Show the latest log entries for the given subscription
					String subscription_id = Env.getArg(0);
					
					PushSubscription push = Env.getUser().getPushSubscription(subscription_id);
					
					Log subscription_log = push.getLog();
					if (subscription_log.getCount() == 0) {
						System.out.println("No log entries found for subscription " + subscription_id + ".");
					} else {
						// Walk through the entries backwards, so the latest is shown last
						for (LogEntry l : subscription_log) {
							System.out.println(
									new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(l.getRequestTime()) + " " +
									(l.getSuccess() ? "Success " : "") +
									l.getMessage());
						}
					}
					break;
					
				default:
					System.err.println("Only one subscription ID can be specified!");
					break;
			}
		} catch (NumberFormatException e) {
			System.err.println("Invalid subscription ID!");
		} catch (EAPIError e) {
			System.err.println("APIError: " + e.getMessage());
		} catch (EAccessDenied e) {
			System.err.println("APIError: " + e.getMessage());
		} catch (EInvalidData e) {
			System.err.println("APIError: " + e.getMessage());
		}
		
		System.out.println("Rate-limit-remaining: " + Env.getUser().getRateLimitRemaining());
	}
}
