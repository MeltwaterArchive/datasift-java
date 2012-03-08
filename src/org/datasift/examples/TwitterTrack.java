/**
 * This example mimics the Twitter track functionality. Run the script with
 * any number of words or phrases as arguments and the script will create
 * the equivalent CSDL and consume it as a stream, displaying matching
 * interactions as they come in.
 */
package org.datasift.examples;

import org.datasift.*;

/**
 * @author MediaSift
 * @version 0.1
 */
public class TwitterTrack implements IStreamConsumerEvents {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			if (args.length == 0) {
				System.out
						.println("ERR: Please specify the words and/or phrases to track!");
				System.exit(1);
			}

			// Authenticate
			System.out.println("Creating user...");
			User user = new User(Config.username, Config.api_key);

			// Create the definition
			StringBuilder sb = new StringBuilder(
					"interaction.type == \"twitter\" and (interaction.content contains \"");
			sb.append(args[0]);
			for (int i = 1; i < args.length; i++) {
				sb.append("\" or interaction.content contains \"").append(
						args[i]);
			}
			sb.append("\")");
			String csdl = sb.toString();
			System.out.println("Creating definition...");
			System.out.println("  " + csdl);
			Definition def = user.createDefinition(csdl);
			
			System.out.println(def.getHash());

			// Create the consumer
			System.out.println("Getting the consumer...");
			StreamConsumer consumer = def.getConsumer(StreamConsumer.TYPE_HTTP,
					new TwitterTrack());

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
		// Ignored for this example
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
