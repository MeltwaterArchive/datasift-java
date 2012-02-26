/**
 * This simple example demonstrates consuming a stream using the stream hash.
 */
package org.datasift.examples;

import org.datasift.*;

/**
 * @author MediaSift
 * @version 0.1
 */
public class ConsumeStream implements IStreamConsumerEvents {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			if (args.length != 1) {
				System.out
						.println("ERR: Please specify the hash to consume");
				System.exit(1);
			}

			// Authenticate
			System.out.println("Creating user...");
			User user = new User(Config.username, Config.api_key);

			// Create the consumer
			System.out.println("Getting the consumer...");
			StreamConsumer consumer = user.getConsumer(StreamConsumer.TYPE_HTTP, args[0],
					new ConsumeStream());

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
		try {
			System.out.print(i.getStringVal("interaction.author.username"));
			System.out.print(": ");
			System.out.println(i.getStringVal("interaction.content"));
		} catch (EInvalidData e) {
			// The interaction did not contain either a type or content.
			System.out.println("Exception: " + e.getMessage());
			System.out.print("Interaction: ");
			System.out.println(i);
		}
		System.out.println("--");
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
		try {
			System.out.print("Deleted: ");
			System.out.print(i.getStringVal("interaction.id"));
		} catch (EInvalidData e) {
			// The interaction did not contain either a type or content.
			System.out.println("Exception: " + e.getMessage());
			System.out.print("Deletion: ");
			System.out.println(i);
		}
		System.out.println("--");
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
}
