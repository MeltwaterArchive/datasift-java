/**
 * This example reads a CSDL definition from a file given on the filename,
 * consumes it for an hour and then fetches usage information for that
 * stream and displays it in a nice GUI.
 */
package org.datasift.examples;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;

import org.datasift.*;

/**
 * @author MediaSift
 * @version 0.1
 */
public class StreamUsage implements IStreamConsumerEvents {
	
	public static User _user = null;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("Please specify the filename of a CSDL definition and an optional run time in seconds.");
			return;
		}
		
		try {
			
			String csdl = StreamUsage.readFileAsString(args[0]).trim();
			
			int run_secs = 3600;
			if (args.length == 2) {
				run_secs = Integer.parseInt(args[1]);
			}
			
			// Authenticate
			System.out.println("Creating user...");
			_user = new User(Config.username, Config.api_key);

			// Create the definition
			System.out.println("Creating definition...");
			System.out.println("  " + csdl);
			Definition def = _user.createDefinition(csdl);

			// Create the consumer
			System.out.println("Getting the consumer...");
			StreamConsumer consumer = def.getConsumer(StreamConsumer.TYPE_HTTP,
					new StreamUsage(run_secs));

			// And start consuming
			System.out.println("Consuming...");
			System.out.println("--");
			consumer.consume();
		} catch (EInvalidData e) {
			System.out.print("InvalidData: ");
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.print("IOException: ");
			System.out.println(e.getMessage());
		} catch (ECompileFailed e) {
			System.out.print("CompileFailed: ");
			System.out.println(e.getMessage());
		} catch (EAccessDenied e) {
			System.out.print("AccessDenied: ");
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * 
	 * @param filePath
	 * @return
	 * @throws java.io.IOException
	 */
	private static String readFileAsString(String filePath) throws java.io.IOException{
	    byte[] buffer = new byte[(int) new File(filePath).length()];
	    BufferedInputStream f = null;
	    try {
	        f = new BufferedInputStream(new FileInputStream(filePath));
	        f.read(buffer);
	    } finally {
	        if (f != null) try { f.close(); } catch (IOException ignored) { }
	    }
	    return new String(buffer);
	}

	private long _end_time = -1;
	
	/**
	 * Constructor. Initialises the time we should stop consuming.
	 */
	public StreamUsage(int run_for_secs)
	{
		_end_time = System.currentTimeMillis() + (run_for_secs * 1000);
	}

	/**
	 * Called when the connection has been established.
	 * 
	 * @param StreamConsumer consumer The consumer object.
	 */
	public void onConnect(StreamConsumer c) {
		System.out.println("Connected");
		System.out.println("--");
	}
	
	/**
	 * Called when the connection has disconnected.
	 * 
	 * @param StreamConsumer consumer The consumer object.
	 */
	public void onDisconnect(StreamConsumer c) {
		System.out.println("Disconnected");
		System.out.println("--");
	}

	/**
	 * Handle incoming data.
	 * 
	 * @param StreamConsumer
	 *            consumer The consumer object.
	 * @param JSONObject
	 *            interaction The interaction data.
	 * @throws EInvalidData
	 */
	public void onInteraction(StreamConsumer c, Interaction i)
			throws EInvalidData {
		try {
			System.out.print(i.getStringVal("interaction.author.name"));
			System.out.print(": ");
			System.out.println(i.getStringVal("interaction.content"));
		} catch (EInvalidData e) {
			// The interaction did not contain either a type or content.
			System.out.println("Exception: " + e.getMessage());
			System.out.print("Interaction: ");
			System.out.println(i);
		}
		System.out.println("--");
		
		if (System.currentTimeMillis() > _end_time) {
			c.stop();
		}
	}

	/**
	 * Handle delete notifications.
	 * 
	 * @param StreamConsumer
	 *            consumer The consumer object.
	 * @param JSONObject
	 *            interaction The interaction data.
	 * @throws EInvalidData
	 */
	public void onDeleted(StreamConsumer c, Interaction i)
			throws EInvalidData {
		// Ignored for this example
	}

	/**
	 * Called when the consumer has stopped.
	 * 
	 * @param DataSift_StreamConsumer
	 *            consumer The consumer object.
	 * @param string
	 *            reason The reason the consumer stopped.
	 */
	public void onStopped(StreamConsumer consumer, String reason) {
		System.out.print("Stopped: ");
		System.out.println(reason);
		
		// Get the usage and display it
		try {
			Usage u = _user.getUsage(User.USAGE_HOUR);
			System.out.println("Usage information from " + u.getStartDate() + " to " + u.getEndDate() + "...");
			for (String hash : u.getStreamHashes()) {
				System.out.println("  Stream " + hash + ":");
				System.out.println("    Consumed for " + u.getSeconds(hash) + " seconds");
				System.out.println("    Received...");
				for (String type : u.getLicenseTypes(hash)) {
					int num = u.getLicenseUsage(hash, type);
					System.out.println("      " + num + " " + type + " interaction" + (num == 1 ? "" : "s"));
				}
			}
		} catch (EAPIError e) {
			System.out.print("APIError: ");
			System.out.println(e.getMessage());
		} catch (EAccessDenied e) {
			System.out.print("AccessDenied: ");
			System.out.println(e.getMessage());
		} catch (EInvalidData e) {
			System.out.print("InvalidData: ");
			System.out.println(e.getMessage());
		} catch (ParseException e) {
			System.out.print("ParseException: ");
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Called when a warning is received in the data stream.
	 * 
	 * @param DataSift_StreamConsumer consumer The consumer object.
	 * @param string message The warning message.
	 */
	public void onWarning(StreamConsumer consumer, String message)
			throws EInvalidData {
		System.out.println("Warning: " + message);
	}

	/**
	 * Called when an error is received in the data stream.
	 * 
	 * @param DataSift_StreamConsumer consumer The consumer object.
	 * @param string message The error message.
	 */
	public void onError(StreamConsumer consumer, String message)
			throws EInvalidData {
		System.out.println("Error: " + message);
	}
}
