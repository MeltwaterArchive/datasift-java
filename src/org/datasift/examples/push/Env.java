package org.datasift.examples.push;

import java.util.ArrayList;

import org.datasift.EInvalidData;
import org.datasift.PushOutputParams;
import org.datasift.PushSubscription;
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
	
	static public void displaySubscriptionDetails(PushSubscription subscription) {
	    System.out.println("ID:            " + subscription.getId());
	    System.out.println("Name:          " + subscription.getName());
	    System.out.println("Status:        " + subscription.getStatus());
	    System.out.println("Created at:    " + subscription.getCreatedAt());
	    System.out.println("Last request:  " + subscription.getLastRequest());
	    System.out.println("Last success:  " + subscription.getLastSuccess());
	    System.out.println("Output Type:   " + subscription.getOutputType());
	    
		System.out.println("Output Params:");
		PushOutputParams output_params = subscription.getOutputParams();
	    for (String key : output_params.keySet()) {
	    	System.out.println("  " + key + " = " + output_params.get(key));
	    }
	}
}
