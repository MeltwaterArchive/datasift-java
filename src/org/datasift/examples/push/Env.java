package org.datasift.examples.push;

import java.util.ArrayList;

import org.datasift.EInvalidData;
import org.datasift.PushSubscription;
import org.datasift.User;
import org.datasift.pushsubscription.HttpOutputType;

public class Env {
	static private User _user = null;
	static private ArrayList<String> _args = null;
	
	static public void init(String[] args) {
		// Using the integration environment
		User._api_base_url = "api.integrationdatasift.com/";
		User._stream_base_url = "stream.integrationdatasift.com/";
		User._websocket_base_url = "websocket.integrationdatasift.com/";
		
		// Using the sdallas0 environment
//		User._api_base_url = "api.fido.sdallas0/";
//		User._stream_base_url = "stream.fido.sdallas0/";
//		User._websocket_base_url = "websocket.fido.sdallas0/";

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
	    String output_type = subscription.getOutputType();
	    System.out.println("ID:            " + String.valueOf(subscription.getId()));
	    System.out.println("Name:          " + subscription.getName());
	    System.out.println("Status:        " + subscription.getStatus());
	    System.out.println("Created at:    " + subscription.getCreatedAt());
	    System.out.println("Output Type:   " + output_type);
	    
	    if (output_type.toLowerCase().equals("http")) {
			System.out.println("Output Params: url       = " + ((HttpOutputType)subscription).getUrl()); 
		    System.out.println("               max_post  = " + ((HttpOutputType)subscription).getMaxSize() + " bytes");
		    System.out.println("               auth type = " + ((HttpOutputType)subscription).getAuthType());
		    if (!((HttpOutputType)subscription).getAuthType().toLowerCase().equals("none")) {
			    System.out.println("               auth user = " + ((HttpOutputType)subscription).getAuthUsername());
			    System.out.println("               auth pass = " + ((HttpOutputType)subscription).getAuthPassword());
		    }
	    }
	}
}
