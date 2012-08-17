/**
 * This example lists the current Historics queries in your account.
 */
package org.datasift.examples.historics;

import java.util.ArrayList;
import java.util.Iterator;

import org.datasift.EAPIError;
import org.datasift.EAccessDenied;
import org.datasift.EInvalidData;
import org.datasift.Historic;

public class List {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			// Set up the environment
			Env.init(args);
			
			// Get historics
			ArrayList<Historic> historics = Env.getUser().listHistorics();
			
			// Did we get any?
			if (historics.size() == 0) {
				System.out.println("No Historics queries exist in your account.");
			} else {
				// Display the details
				Iterator<Historic> i = historics.iterator();
				while (i.hasNext()) {
				    Env.displayHistoricDetails(i.next());
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
