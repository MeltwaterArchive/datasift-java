/**
 * This example views the details of a Historics query in your account.
 */
package org.datasift.examples.historics;

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
		
		// Make sure we have at least one Historics playback ID
		if (Env.getArgCount() == 0) {
			System.err.println("Please specify one or more playback IDs to view!");
			System.exit(1);
		}
		
		// View the subscriptions given on the command line
		for (int i = 0; i < Env.getArgCount(); i++) {
			try {
				String playback_id = Env.getArg(i);
				
				Env.displayHistoricDetails(Env.getUser().getHistoric(playback_id));
				System.out.println("--");
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
