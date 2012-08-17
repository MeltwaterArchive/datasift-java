/**
 * This example stops one or more Historics queries in your account.
 */
package org.datasift.examples.historics;

import org.datasift.EAPIError;
import org.datasift.EAccessDenied;
import org.datasift.EInvalidData;
import org.datasift.Historic;

public class Stop {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Set up the environment
		Env.init(args);
		
		// Make sure we have at least one subscription ID
		if (Env.getArgCount() == 0) {
			System.err.println("Please specify one or more Historics queries to stop!");
			System.exit(1);
		}
		
		// Delete the subscriptions given on the command line
		for (int i = 0; i < Env.getArgCount(); i++) {
			try {
				String playback_id = Env.getArg(i);
				
				Historic historic = Env.getUser().getHistoric(playback_id);
				System.out.print("Stopping " + playback_id + ", " + historic.getName() + "...");
				historic.stop();
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
