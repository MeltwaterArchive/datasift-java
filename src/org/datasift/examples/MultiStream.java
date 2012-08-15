package org.datasift.examples;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.datasift.Config;
import org.datasift.EAPIError;
import org.datasift.EAccessDenied;
import org.datasift.ECompileFailed;
import org.datasift.EInvalidData;
import org.datasift.IMultiStreamConsumerEvents;
import org.datasift.Interaction;
import org.datasift.JSONdn;
import org.datasift.StreamConsumer;
import org.datasift.User;

public class MultiStream implements IMultiStreamConsumerEvents {

	private User _user = null;
	
	private HashMap<String, String> _hashes = new HashMap<String, String>();

	private int _counter = 0;
	
	private String _java_stream_hash = null;
	
	private boolean _had_java = false;

	/**
	 * @param args
	 * @throws IOException
	 * @throws MalformedURLException
	 */
	public static void main(String[] args) {
		new MultiStream().run();
	}

	protected void run() {
		try {
			// Authenticate
			System.out.println("Creating user...");
			_user = new User(Config.username, Config.api_key);

			// Building the hash list. All we actually need is an
			// ArrayList<String> of hashes, but we'll use a HashMap so we can
			// prepend the output with which stream the interaction matched
			System.out.println("Building hash list...");

			// We're going to watch for anything containing either "python" or
			// "php". Then, after 10 interactions we'll also subscribe to a stream with anything containing "java". After 10 further interactions we'll unsubscribe from the "java" stream.
			_hashes.put(
					_user.createDefinition(
							"interaction.content contains \"python\"")
							.getHash(), "Python");
			_hashes.put(
					_user.createDefinition(
							"interaction.content contains \"php\"").getHash(),
					"PHP");
			
			// This is the definition for "java"
			_java_stream_hash = _user.createDefinition("interaction.content contains \"java\"").getHash();

			// Create the consumer
			System.out.println("Getting the consumer...");
			StreamConsumer consumer = StreamConsumer.factory(_user,
					StreamConsumer.TYPE_HTTP_MULTI, new ArrayList<String>(
							_hashes.keySet()), this);

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
	 * @param String
	 *            hash The hash of the stream that matched this interaction.
	 * @param Interaction
	 *            interaction The interaction data.
	 * @throws EInvalidData
	 */
	public void onInteraction(StreamConsumer c, String hash, Interaction i)
			throws EInvalidData {
		if (i.has("interaction.author.username")) {
			_counter++;
			System.out.print(String.valueOf(_counter) + " ");
			try {
				if (_hashes != null && _hashes.containsKey(hash)) {
					System.out.print("[" + _hashes.get(hash) + "] ");
					if (_hashes.get(hash).equals("Java")) {
						_had_java = true;
					}
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
	
			// When we've had 10 interactions, subscribe to "java"
			if (_counter == 10) {
				try {
					_hashes.put(_java_stream_hash, "Java");
					c.subscribe(_java_stream_hash);
				} catch (EAPIError e) {
					System.out.print("APIError: ");
					System.out.println(e.getMessage());
				}
			}
	
			// When we've had 20 interactions, unsubscribe from "java" but only if we've had a Java interaction
			if (_counter == 20 && _had_java) {
				try {
					_hashes.remove(_java_stream_hash);
					c.unsubscribe(_java_stream_hash);
				} catch (EAPIError e) {
					System.out.print("APIError: ");
					System.out.println(e.getMessage());
				}
			}
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
	public void onDeleted(StreamConsumer c, String hash, Interaction i)
			throws EInvalidData {
		// Ignored for this example
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
