/**
 * This simple example demonstrates consuming a stream using the stream hash.
 */
package org.datasift.examples;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.datasift.*;

/**
 * @author MediaSift
 * @version 0.1
 */
public class HistoricDump implements IStreamConsumerEvents {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Config
		String csdl  = "interaction.type == \"twitter\"";
		String start = "2012-05-23 12:00:00";
		String end   = "2012-05-23 13:00:00";
		String feeds = "twitter";
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		try {
			// Authenticate
			System.err.println("Creating user...");
			User user = new User(Config.username, Config.api_key, false);

			// Create the definition
			Definition d = user.createDefinition(csdl);
			
			// Create the historic
			Historic h = d.createHistoric(df.parse(start), df.parse(end), feeds, 100);
			
			// Create the consumer
			System.err.println("Getting the consumer...");
			StreamConsumer consumer = h.getConsumer(StreamConsumer.TYPE_HTTP, new HistoricDump(h));

			// And start consuming
			System.err.println("Consuming...");
			System.err.println("--");
			consumer.consume();
		} catch (EInvalidData e) {
			System.err.print("InvalidData: ");
			System.err.println(e.getMessage());
		} catch (ECompileFailed e) {
			System.err.print("CompileFailed: ");
			System.err.println(e.getMessage());
		} catch (EAccessDenied e) {
			System.err.print("AccessDenied: ");
			System.err.println(e.getMessage());
		} catch (EAPIError e) {
			System.err.print("EAPIError: ");
			System.err.println(e.getMessage());
		} catch (ParseException e) {
			System.err.print("ParseException: ");
			System.err.println(e.getMessage());
		}
	}
	
	private Historic _h = null;
	
	public HistoricDump(Historic h) {
		_h = h;
	}

	/**
	 * Called when the connection has been established.
	 * 
	 * @param StreamConsumer consumer The consumer object.
	 */
	public void onConnect(StreamConsumer c) {
		System.err.println("Connected, starting historic query");
		
		// Start the historic query
		try {
			_h.start();
			System.err.println("--");
		} catch (EInvalidData e) {
			System.err.print("EInvalidData: ");
			System.err.println(e.getMessage());
		} catch (EAccessDenied e) {
			System.err.print("EAccessDenied: ");
			System.err.println(e.getMessage());
		} catch (EAPIError e) {
			System.err.print("EAPIError: ");
			System.err.println(e.getMessage());
		}
	}
	
	/**
	 * Called when the connection has disconnected.
	 * 
	 * @param StreamConsumer consumer The consumer object.
	 */
	public void onDisconnect(StreamConsumer c) {
		System.err.println("Disconnected");
		System.err.println("--");
	}

	/**
	 * Handle incoming data.
	 * 
	 * @param StreamConsumer
	 *            consumer The consumer object.
	 * @param Interaction
	 *            interaction The interaction data.
	 * @throws EInvalidData
	 */
	public void onInteraction(StreamConsumer c, Interaction i) {
		try {
			System.out.print(i.getStringVal("interaction.id"));
		} catch (EInvalidData e) {
			// Ignored
		}
		
		System.out.print(",\"");
		
		try {
			System.out.print(i.getStringVal("interaction.created_at").replace("\"", "\\\""));
		} catch (EInvalidData e) {
			// Ignored
		}
		
		System.out.print("\",\"");
		
		try {
			System.out.print(i.getStringVal("interaction.author.username").replace("\"", "\\\""));
		} catch (EInvalidData e) {
			// Ignored
		}
		
		System.out.print("\",\"");
		
		try {
			System.out.print(i.getStringVal("interaction.content").replace("\"", "\\\""));
		} catch (EInvalidData e) {
			// Ignored
		}
		
		System.out.println("\"");
	}

	/**
	 * Handle delete notifications.
	 * 
	 * @param StreamConsumer
	 *            consumer The consumer object.
	 * @param Interaction
	 *            interaction The interaction data.
	 * @throws EInvalidData
	 */
	public void onDeleted(StreamConsumer c, Interaction i)
			throws EInvalidData {
		// For the purposes of this example we do nothing with deletes, but in
		// your implementation you will need to properly process deletes by
		// removing them from your storage system.
	}

	/**
	 * Handle status notifications
	 * 
	 * @param StreamConsumer
	 *            consumer The consumer object.
	 * @param String
	 *            type The status notification type.
	 * @param JSONdn
	 *            info The notification data.
	 */
	public void onStatus(StreamConsumer consumer, String type, JSONdn info) {
		if (type.equalsIgnoreCase("info") && info.has("progress")) {
			try {
				System.err.println(info.getStringVal("progress") + "% done");
			} catch (EInvalidData e) {
				// Failed to get the progress string, ignore it
			}
		} else {
			System.err.println("STATUS: " + type);
			try {
				_h.reloadData();
				System.err.println("STATUS: " + _h.getStatus() + " - " + String.valueOf(_h.getProgress()) + "% done");
			} catch (EInvalidData e) {
				System.err.println("reloadData: EInvalidData: " + e.getMessage());
			} catch (EAccessDenied e) {
				System.err.println("reloadData: EAccessDenied: " + e.getMessage());
			} catch (EAPIError e) {
				System.err.println("reloadData: EAPIError: " + e.getMessage());
			}
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
		System.err.println("Warning: " + message);
	}

	/**
	 * Called when an error is received in the data stream.
	 * 
	 * @param DataSift_StreamConsumer consumer The consumer object.
	 * @param string message The error message.
	 */
	public void onError(StreamConsumer consumer, String message)
			throws EInvalidData {
		System.err.println("Error: " + message);
	}

	/**
	 * Called when the consumer has stopped.
	 * 
	 * @param DataSift_StreamConsumer
	 *            $consumer The consumer object.
	 * @param string
	 *            $reason The reason the consumer stopped.
	 */
	public void onStopped(StreamConsumer consumer, String reason) {
		System.err.print("Stopped: " + reason);
	}
}
