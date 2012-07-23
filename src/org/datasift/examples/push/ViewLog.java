/**
 * This example displays the logs for all subscriptions in your account, or
 * just those related to a specific subscription.
 */
package org.datasift.examples.push;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.datasift.EAPIError;
import org.datasift.EAccessDenied;
import org.datasift.EInvalidData;
import org.datasift.PushSubscription;
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
					ArrayList<LogEntry> logs = Env.getUser().getPushSubscriptionLogs();
					
					if (logs.size() == 0) {
						System.out.println("No log entries found.");
					} else {
						// Walk through the entries backwards, so the latest is shown last
						for (int i = logs.size() - 1; i >= 0; i--) {
							LogEntry l = logs.get(i);
							System.out.println(
									new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(l.getRequestTime()) + " [" +
									String.valueOf(l.getSubscriptionId()) + "] " +
									(l.getSuccess() ? "Success " : "") +
									l.getMessage());
						}
					}
					break;
					
				case 1:
					// Show the latest log entries for the given subscription
					String subscription_id = Env.getArg(0);
					
					PushSubscription push = Env.getUser().getPushSubscription(subscription_id);
					
					ArrayList<LogEntry> log = push.getLog();
					if (log.size() == 0) {
						System.out.println("No log entries found for subscription " + subscription_id + ".");
					} else {
						// Walk through the entries backwards, so the latest is shown last
						for (int i = log.size() - 1; i >= 0; i--) {
							LogEntry l = log.get(i);
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
	}
}
