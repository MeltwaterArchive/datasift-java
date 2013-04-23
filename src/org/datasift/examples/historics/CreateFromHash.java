/**
 * This example creates a Historics query in your account.
 */
package org.datasift.examples.historics;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.datasift.EAccessDenied;
import org.datasift.EInvalidData;
import org.datasift.Historic;

public class CreateFromHash {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Set up the environment
		Env.init(args);
		
		// Check we have enough arguments
		if (Env.getArgCount() != 6) {
			usage();
		}

		// Get the args
		String stream_hash = Env.getArg(0);
		String start_date  = Env.getArg(1);
		String end_date    = Env.getArg(2);
		String sources     = Env.getArg(3);
		double sample      = Double.parseDouble(Env.getArg(4));
		String name        = Env.getArg(5);
		
		// Parse the dates from the command line
		DateFormat df    = new SimpleDateFormat("yyyyMMddHHmmss");
		Date       start = null;
		Date       end   = null;
		try {
			start = df.parse(start_date);
		} catch (ParseException e2) {
			System.err.println("Invalid start date: " + Env.getArg(1));
			System.exit(1);
		}
		try {
			end = df.parse(end_date);
		} catch (ParseException e2) {
			System.err.println("Invalid end date: " + Env.getArg(1));
			System.exit(1);
		}
		
		try {
			// Create the Historic
			Historic historic = Env.getUser().createHistoric(stream_hash, start, end, sources, sample, name);

			// Display the details of the new historic
			Env.displayHistoricDetails(historic);
		} catch (EInvalidData e) {
			System.err.println("EInvalidData: " + e.getMessage());
		} catch (EAccessDenied e) {
			System.err.println("EAccessDenied: " + e.getMessage());
		}
		
		System.out.println("Rate-limit-remaining: " + Env.getUser().getRateLimitRemaining());
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
		System.err.println("Usage: CreateFromHash <username> <api_key> \\");
		System.err.println("             <stream_hash> <start_date> <end_date> <sources> <sample> <name>");
		System.err.println("");
		System.err.println("Where: stream_hash = the stream hash the Historics query should run");
		System.err.println("       start_date  = Historics query start date (yyyymmddhhmmss)");
		System.err.println("       end_date    = Historics query end date (yyyymmddhhmmss)");
		System.err.println("       sources     = the data sources the Historics query should match");
		System.err.println("       sample      = what percentage of the matching interactions are required");
		System.err.println("       name        = a friendly name for the Historics query");
		System.err.println("");
		System.err.println("Example");
		System.err.println("       CreateFromHash <hash> 20120801120000 20120801130000 twitter 100 \\");
		System.err.println("                         \"My Historics query\"");
		System.err.println("");
		if (exit) {
			System.exit(1);
		}
	}
}
