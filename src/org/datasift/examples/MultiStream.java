package org.datasift.examples;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.datasift.Config;
import org.datasift.EAccessDenied;
import org.datasift.ECompileFailed;
import org.datasift.EInvalidData;
import org.datasift.IMultiStreamConsumerEvents;
import org.datasift.Interaction;
import org.datasift.StreamConsumer;
import org.datasift.User;

public class MultiStream implements IMultiStreamConsumerEvents {

	private HashMap<String, String> _hashes = null;

	/**
	 * @param args
	 * @throws IOException
	 * @throws MalformedURLException
	 */
	public static void main(String[] args) {

		try {
			// Authenticate
			System.out.println("Creating user...");
			User user = new User(Config.username, Config.api_key);

			// Building the hash list. All we actually need is an
			// ArrayList<String> of hashes, but we'll use a HashMap so we can
			// prepend the output with which stream the interaction matched
			System.out.println("Building hash list...");
			HashMap<String, String> hashes = new HashMap<String, String>();

			// We're going to watch for anything containing either "python",
			// "php" or "java"
			// Yes, this could easily be done as a single stream, but this is an
			// example of the multi-stream feature!
			hashes.put(
					user.createDefinition(
							"interaction.content contains \"python\"")
							.getHash(), "Python");
			hashes.put(
					user.createDefinition(
							"interaction.content contains \"php\"").getHash(),
					"PHP");
			hashes.put(
					user.createDefinition(
							"interaction.content contains \"java\"").getHash(),
					"Java");

			// Create the consumer
			System.out.println("Getting the consumer...");
			StreamConsumer consumer = StreamConsumer.factory(user,
					StreamConsumer.TYPE_HTTP_MULTI, new ArrayList<String>(
							hashes.keySet()), new MultiStream(hashes));

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

	protected MultiStream(HashMap<String, String> hashes) {
		_hashes = hashes;
	}

	/**
	 * Handle incoming data.
	 * 
	 * @param StreamConsumer
	 *            consumer The consumer object.
	 * @param String
	 *            hash The hash of the stream that matched this interaction.
	 * @param Interaction
	 *            interaction The interaction data.
	 * @throws EInvalidData
	 */
	public void onInteraction(StreamConsumer c, String hash, Interaction i)
			throws EInvalidData {
		try {
			if (_hashes != null && _hashes.containsKey(hash)) {
				System.out.print("[" + _hashes.get(hash) + "] ");
			}
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
