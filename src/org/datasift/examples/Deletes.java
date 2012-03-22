/**
 * This simple example demonstrates consuming a stream using the stream hash.
 */
package org.datasift.examples;

import org.datasift.*;

/**
 * @author MediaSift
 * @version 0.1
 */
public class Deletes implements IStreamConsumerEvents {
	
	private int _line_len = 0;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			// Authenticate
			System.out.println("Creating user...");
			User user = new User(Config.username, Config.api_key);

			// Create the definition
			Definition d = user.createDefinition("interaction.sample < 1.0");
			
			// Create the consumer
			System.out.println("Getting the consumer...");
			StreamConsumer consumer = d.getConsumer(StreamConsumer.TYPE_HTTP, new Deletes());

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
		System.out.print(".");
		incLineLen();
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
		System.out.print("X");
		incLineLen();
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
	
	private void incLineLen() {
		_line_len++;
		if (_line_len > 80) {
			System.out.println();
			_line_len = 0;
		}
	}
}
