package org.datasift.examples.historics;

import java.util.ArrayList;

import org.datasift.EAPIError;
import org.datasift.EAccessDenied;
import org.datasift.EInvalidData;
import org.datasift.Historic;
import org.datasift.User;

public class Env {
	static private User _user = null;
	static private ArrayList<String> _args = null;
	
	static public void init(String[] args) {
		// Make sure we have credentials on the command line
		if (args.length < 2) {
			System.out.println("Please specify your DataSift username and API key as the first two command line arguments!");
			System.exit(1);
		}
		
		try {
			_user = new User(args[0], args[1]);
		} catch (EInvalidData e) {
			System.out.println("Failed to create the User object - check your username and API key!");
			System.exit(1);
		}
		
		_args = new ArrayList<String>();
		int skip = 2;
		for (String arg : args) {
			if (skip > 0) {
				skip--;
			} else {
				_args.add(arg);
			}
		}
	}
	
	static public User getUser() {
		return _user;
	}
	
	static public int getArgCount() {
		return _args.size();
	}
	
	static public String getArg(int idx) {
		return _args.get(idx);
	}
	
	static public void displayHistoricDetails(Historic historic) {
	    try {
			System.out.println("Playback ID:   " + historic.getHash());
		    System.out.println("Stream hash:   " + historic.getStreamHash());
		    System.out.println("Name:          " + historic.getName());
		    System.out.println("Start time:    " + historic.getStartDate());
		    System.out.println("End time:      " + historic.getEndDate());
		    System.out.println("Source" + (historic.getSources().size() == 1 ? ": " : "s:") + "       " + historic.getSources());
		    System.out.println("Created at:    " + historic.getCreatedAt());
		    System.out.println("Status:        " + historic.getStatus());
		    System.out.println("Progress:      " + historic.getProgress() + "%");
		} catch (EInvalidData e) {
			System.err.println("EInvalidData: " + e.getMessage());
		} catch (EAccessDenied e) {
			System.err.println("EAccessDenied: " + e.getMessage());
		} catch (EAPIError e) {
			System.err.println("EAPIError: " + e.getMessage());
		}
	}
}
