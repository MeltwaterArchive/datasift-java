/**
 * This example constructs a Definition object with CSDL that looks for
 * anything containing the word "football". It then gets an HTTP consumer for
 * that definition and displays matching interactions to the screen as they
 * come in. It will display 10 interactions and then stop.
 */
package org.datasift.examples;

import org.datasift.*;

/**
 * @author MediaSift
 * @version 0.1
 */
public class Football implements IStreamConsumerEvents {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			// Authenticate
			System.out.println("Creating user...");
			User user = new User(Config.username, Config.api_key);

			// Create the definition
			String csdl = "interaction.content contains \"football\"";
			System.out.println("Creating definition...");
			System.out.println("  " + csdl);
			Definition def = user.createDefinition(csdl);

			// Create the consumer
			System.out.println("Getting the consumer...");
			StreamConsumer consumer = def.getConsumer(StreamConsumer.TYPE_HTTP,
					new Football());

			// And start consuming
			System.out.println("Consuming...");
			System.out.println("--");
			consumer.consume();
		} catch (EInvalidData e) {
			System.out.print("InvalidData: ");
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
	 * This determines the number of interactions to consume before stopping.
	 */
	private int _num = 10;

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
	 * @param Interaction
	 *            interaction The interaction data.
	 * @throws EInvalidData
	 */
	public void onInteraction(StreamConsumer c, Interaction i)
			throws EInvalidData {
		try {
			System.out.print("Type: ");
			System.out.print(i.getStringVal("interaction.type"));
			System.out.print(", ID: ");
			System.out.println(i.getStringVal("interaction.id"));
			System.out.print("Content: ");
			System.out.println(i.getStringVal("interaction.content"));
		} catch (EInvalidData e) {
			System.out.println("Exception: " + e.getMessage());
			System.out.print("Interaction: ");
			System.out.println(i);
		}

		System.out.println("--");

		// Stop after 10
		if (_num-- == 1) {
			System.out.println("Stopping consumer...");
			c.stop();
		}
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
		try {
			System.out.print("Deleted: ");
			System.out.println(i.getStringVal("interaction.id"));
		} catch (EInvalidData e) {
			// The interaction did not contain either a type or content.
			System.out.println("Exception: " + e.getMessage());
			System.out.print("Deletion: ");
			System.out.println(i);
		}
		System.out.println("--");
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
		System.out.print("STATUS: ");
		System.out.println(type);
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
		System.out.print("Stopped: ");
		System.out.println(reason);
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
